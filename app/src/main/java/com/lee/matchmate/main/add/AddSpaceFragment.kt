package com.lee.matchmate.main.add

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
import com.lee.matchmate.databinding.FragmentAddSpaceBinding
import com.lee.matchmate.main.detail.DetailViewModel

class AddSpaceFragment : ViewBindingBaseFragment<FragmentAddSpaceBinding>(FragmentAddSpaceBinding::inflate) {
    private val viewModel: AddSpaceViewModel by viewModels()

    companion object {
        fun newInstance() = AddSpaceFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAdd.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

}