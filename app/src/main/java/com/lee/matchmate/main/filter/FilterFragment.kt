package com.lee.matchmate.main.filter

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lee.matchmate.R
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentFilterBinding
import com.lee.matchmate.main.MainViewModel

class FilterFragment : ViewBindingBaseFragment<FragmentFilterBinding>(FragmentFilterBinding::inflate) {

    private val viewModel : FilterViewModel by viewModels()
    companion object {
        fun newInstance() = FilterFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbFilter.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}