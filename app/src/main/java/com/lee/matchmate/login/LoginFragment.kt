package com.lee.matchmate.login

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.lee.matchmate.R
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.common.toastMessage
import com.lee.matchmate.databinding.FragmentLoginBinding

class LoginFragment : ViewBindingBaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    companion object {
        fun newInstance() = LoginFragment()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLogin.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToMainFragment()
            findNavController().navigate(action)
        }
        super.onViewCreated(view, savedInstanceState)
    }



}

