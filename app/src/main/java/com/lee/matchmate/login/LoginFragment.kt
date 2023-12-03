package com.lee.matchmate.login

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.lee.matchmate.chat.fcm.TAG
import com.lee.matchmate.common.AppGlobalContext
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.common.toastMessage
import com.lee.matchmate.databinding.FragmentLoginBinding
import com.lee.matchmate.main.User

class LoginFragment : ViewBindingBaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = "user"

    companion object {
        fun newInstance() = LoginFragment()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /*        val keyHash = Utility.getKeyHash(requireContext())
                Log.e("Hash",keyHash)*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) alertNotificationPermission()

        val user = User()


        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            //token 값 받아서 preference에 저장
            if (error != null) {
                val errorMessage = when (error.toString()) {
                    AuthErrorCause.AccessDenied.toString() -> {
                        "접근이 거부 됨(동의 취소)"
                    }

                    AuthErrorCause.InvalidClient.toString() -> {
                        "유효하지 않은 앱"
                    }

                    AuthErrorCause.InvalidGrant.toString() -> {
                        "인증 수단이 유효하지 않아 인증할 수 없는 상태"
                    }

                    AuthErrorCause.InvalidRequest.toString() -> {
                        "요청 파라미터 오류"
                    }

                    AuthErrorCause.InvalidScope.toString() -> {
                        "유효하지 않은 scope ID"
                    }

                    AuthErrorCause.Misconfigured.toString() -> {
                        "설정이 올바르지 않음(android key hash)"
                    }

                    AuthErrorCause.ServerError.toString() -> {
                        "카카오 서버 에러"
                    }

                    AuthErrorCause.Unauthorized.toString() -> {
                        "현재 앱은 요청권한이 없음"
                    }

                    else -> { // Unknown
                        "알려지지 않은 에러"
                    }
                }
                toastMessage(errorMessage, activity as Activity)
            } else if (token != null) {

                UserApiClient.instance.me { userInfo, error ->
                    if (error == null) {
                        if (userInfo != null) {
                            user.userName = userInfo.kakaoAccount?.profile?.nickname.toString()
                            user.profileImage =
                                userInfo.kakaoAccount?.profile?.profileImageUrl.toString()

                            Firebase.messaging.token.addOnSuccessListener {
                                user.fcmToken = it
                            }

                            insertFireStore(user, userInfo.id.toString())
                            AppGlobalContext.prefs.edit()
                                .putString("userId", userInfo.id.toString()).apply()
                            AppGlobalContext.prefs.edit()
                                .putBoolean("IS_LOGGED_IN", true).apply()
                            toastMessage(
                                "환영합니다 ${userInfo.kakaoAccount?.profile?.nickname} 님",
                                activity as Activity
                            )
                        }
                    }
                }
                val action = LoginFragmentDirections.actionLoginFragmentToMainFragment()
                findNavController().navigate(action)
            }
        }

        binding.btnLogin.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToMainFragment()
            findNavController().navigate(action)
        }

        binding.btnKakaoLogin.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                UserApiClient.instance.loginWithKakaoTalk(requireContext(), callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun insertFireStore(user: User, userId: String) {
        val documentRef = fireStoreDB.collection(fireStoreCollectionName).document(userId)

        documentRef.get().addOnSuccessListener {
            if (it.exists()) {
                documentRef.update(user.toMap())
            } else {
                documentRef.set(user.toMap()).addOnSuccessListener {

                }.addOnFailureListener {
                    toastMessage("실패", activity as Activity)
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            toastMessage("권한이 허용되었습니다.", activity as Activity)
        } else {


        }
    }
    private fun alertNotificationPermission() {
        // API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                toastMessage("권한이 허용되었습니다.", activity as Activity)
                //퍼미션 허락시 진행할 코드
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                toastMessage("권한이 허용되었습니다.", activity as Activity)
            } else {
                // 직접 퍼미션 요청
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

}

