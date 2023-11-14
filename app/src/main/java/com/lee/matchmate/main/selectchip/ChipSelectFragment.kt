package com.lee.matchmate.main.selectchip

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.jakewharton.rxbinding4.view.clicks
import com.lee.matchmate.R
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentChipSelectBinding
import com.lee.matchmate.main.filter.FilterViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ChipSelectFragment :
    ViewBindingBaseFragment<FragmentChipSelectBinding>(FragmentChipSelectBinding::inflate) {

    companion object {
        fun newInstance() = ChipSelectFragment()
    }

    private val viewModel: ChipSelectViewModel by viewModels()

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSelectChipAdd.clicks().observeOn(Schedulers.io())
            .throttleFirst(500, TimeUnit.MILLISECONDS).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe {
                addConditionChip(binding.edSelectChipAddCond.text.toString())
            }
    }

    private fun addConditionChip(cond: String) {
        if (cond.isNotEmpty()) {
            binding.cgSelectChipListCond.addView(Chip(context).apply {
                text = cond
                isCloseIconVisible = true
                //chip에 있는 close 버튼을 클릭할 때, 삭제한 id 값을 기반을 selectedArtistIdList 값을 삭제함
                setOnCloseIconClickListener {
                    binding.cgSelectChipListCond.removeView(it)
                }
            })
        }
    }

}

