package com.lee.matchmate.login

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.lee.matchmate.R
import com.lee.matchmate.common.AppGlobalContext
import com.lee.matchmate.common.Constants
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.common.toastMessage
import com.lee.matchmate.databinding.FragmentLoginBinding
import com.lee.matchmate.main.User

class LoginFragment : ViewBindingBaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = Constants.USER_COLLECTION_NAME

    companion object {
        fun newInstance() = LoginFragment()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) alertNotificationPermission()
        val user = User()

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            //token 값 받아서 preference에 저장
            if (error != null) {
                val errorMessage = when (error.toString()) {
                    AuthErrorCause.AccessDenied.toString() -> {
                        getString(R.string.access_denied)
                    }

                    AuthErrorCause.InvalidClient.toString() -> {
                        getString(R.string.invalid_app)
                    }

                    AuthErrorCause.InvalidGrant.toString() -> {
                        getString(R.string.invalid_grant)
                    }

                    AuthErrorCause.InvalidRequest.toString() -> {
                        getString(R.string.invalid_request)
                    }

                    AuthErrorCause.InvalidScope.toString() -> {
                        getString(R.string.invalid_scope)
                    }

                    AuthErrorCause.Misconfigured.toString() -> {
                        getString(R.string.misconfigured)
                    }

                    AuthErrorCause.ServerError.toString() -> {
                        getString(R.string.server_error)
                    }

                    AuthErrorCause.Unauthorized.toString() -> {
                        getString(R.string.unauthorized)
                    }

                    else -> { // Unknown
                        getString(R.string.unknown)
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
                                .putString(Constants.USER_ID, userInfo.id.toString()).apply()
                            AppGlobalContext.prefs.edit()
                                .putBoolean(Constants.IS_LOGGED_IN, true).apply()
                            toastMessage(
                                "${Constants.WELCOME_MESSAGE_PREFIX}${userInfo.kakaoAccount?.profile?.nickname}${Constants.WELCOME_MESSAGE_SUFFIX}",
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
                    toastMessage(getString(R.string.fail), activity as Activity)
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            toastMessage(getString(R.string.granted), activity as Activity)
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
                toastMessage(getString(R.string.granted), activity as Activity)
                //퍼미션 허락시 진행할 코드
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                toastMessage(getString(R.string.granted), activity as Activity)
            } else {
                // 직접 퍼미션 요청
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

}

