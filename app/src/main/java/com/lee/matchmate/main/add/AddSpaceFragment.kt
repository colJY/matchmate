package com.lee.matchmate.main.add

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.lee.matchmate.R
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentAddSpaceBinding
import com.lee.matchmate.main.detail.DetailViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AddSpaceFragment : ViewBindingBaseFragment<FragmentAddSpaceBinding>(FragmentAddSpaceBinding::inflate) {
    private val viewModel: AddSpaceViewModel by viewModels()

    companion object {
        fun newInstance() = AddSpaceFragment()
    }


    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAdd.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.chAdd.clicks().observeOn(Schedulers.io())
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val action = AddSpaceFragmentDirections.actionAddSpaceFragmentToChipSelectFragment()
                findNavController().navigate(action)
            }
    }

}