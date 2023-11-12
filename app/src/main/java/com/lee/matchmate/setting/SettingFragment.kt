package com.lee.matchmate.setting

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lee.matchmate.R
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentSettingBinding
import com.lee.matchmate.main.add.AddSpaceViewModel

class SettingFragment : ViewBindingBaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {
    private val viewModel: SettingViewModel by viewModels()
    companion object {
        fun newInstance() = SettingFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



}