package com.lee.matchmate.setting

import android.os.Bundle
import android.view.View
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentSettingBinding

class SettingFragment :
    ViewBindingBaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {
    companion object {
        fun newInstance() = SettingFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}