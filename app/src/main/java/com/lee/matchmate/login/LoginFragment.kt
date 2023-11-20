package com.lee.matchmate.login

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
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


        val user = User()

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                toastMessage("토큰 정보 보기 실패", activity as Activity)
            } else if (tokenInfo != null) {
                toastMessage("토큰 정보 보기 성공", activity as Activity)
                val action = LoginFragmentDirections.actionLoginFragmentToMainFragment()
                findNavController().navigate(action)
            }
        }

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
                toastMessage("로그인 성공", activity as Activity)
                UserApiClient.instance.me { userInfo, error ->
                    if (error != null) {
                        if (userInfo != null) {
                            user.userName = userInfo.kakaoAccount?.profile?.nickname.toString()
                            user.profileImage =
                                userInfo.kakaoAccount?.profile?.profileImageUrl.toString()
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


}

