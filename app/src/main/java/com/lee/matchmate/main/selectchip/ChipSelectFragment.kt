package com.lee.matchmate.main.selectchip

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.jakewharton.rxbinding4.view.clicks
import com.lee.matchmate.R
import com.lee.matchmate.common.Constants
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentChipSelectBinding
import com.lee.matchmate.main.add.AddSpaceViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Chip select fragment
 * 조건 Chip 선택을 위한 Fragment
 */
class ChipSelectFragment :
    ViewBindingBaseFragment<FragmentChipSelectBinding>(FragmentChipSelectBinding::inflate) {

    companion object {
        fun newInstance() = ChipSelectFragment()
    }

    private var selectedCondList: MutableSet<String> = mutableSetOf()
    private val viewModel: ChipSelectViewModel by viewModels()
    private val addSpaceViewModel: AddSpaceViewModel by activityViewModels()

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChip()

        with(binding){
            btnSelectChipAdd
                .clicks()
                .observeOn(Schedulers.io())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    addConditionChip(binding.edSelectChipAddCond.text.toString())
                }

            tbSelectChip.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.menu_filter_check) {
                    addSpaceViewModel.spaceSelectedCondList.postValue(selectedCondList)
                    findNavController().popBackStack()
                    return@setOnMenuItemClickListener true
                }
                false
            }
        }

        addSpaceViewModel.spaceSelectedCondList.observe(viewLifecycleOwner) { _ ->

        }

    }

    private fun addConditionChip(cond: String) {
        if (cond.isNotEmpty()) {
            binding.cgSelectChipAddCond.addView(Chip(context).apply {
                selectedCondList.add(cond)
                text = cond
                isCloseIconVisible = true
                //chip에 있는 close 버튼을 클릭할 때, 삭제한 id 값을 기반을 selectedArtistIdList 값을 삭제함
                setOnCloseIconClickListener {
                    binding.cgSelectChipAddCond.removeView(it)
                    selectedCondList.remove(text.toString())
                }
            })
        }
    }

    private fun initChip() {
        viewModel.chipData.observe(viewLifecycleOwner) { chipData ->
            binding.cgSelectChipListCond.removeAllViews()
            val chipDataList = chipData.split(Constants.SPLIT)
            chipDataList.forEach { chipText ->
                val chip = Chip(context).apply {
                    text = chipText
                    isCheckable = true
                }

                chip.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        selectedCondList.add(chipText)
                    } else {
                        selectedCondList.remove(chipText)
                    }
                }
                binding.cgSelectChipListCond.addView(chip)
            }
        }
    }


}

