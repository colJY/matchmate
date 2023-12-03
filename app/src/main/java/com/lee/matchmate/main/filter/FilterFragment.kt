package com.lee.matchmate.main.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.lee.matchmate.R
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentFilterBinding
import com.lee.matchmate.main.MainViewModel
import com.lee.matchmate.main.selectchip.ChipSelectViewModel

class FilterFragment :
    ViewBindingBaseFragment<FragmentFilterBinding>(FragmentFilterBinding::inflate) {

    private val viewModel: MainViewModel by activityViewModels()
    private val chipViewModel: ChipSelectViewModel by viewModels()

    companion object {
        fun newInstance() = FilterFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var maxValue = ""
        var minValue = ""
        val condList: MutableList<String> = mutableListOf()
        var roomType: String? = null

        with(viewModel) {
            selectedType.value = ""
            selectedMaxValue.value = ""
            selectedMinValue.value = ""
            selectedCondList.value = mutableListOf()
        }

        binding.tbFilter.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.tbFilter.setOnMenuItemClickListener {
            val action = FilterFragmentDirections.actionFilterFragmentToMainFragment()
            with(viewModel) {
                selectedMaxValue.postValue(maxValue)
                selectedMinValue.postValue(minValue)
                selectedCondList.postValue(condList)
                selectedType.postValue(roomType)
                filterData()
            }

            findNavController().navigate(action)
            return@setOnMenuItemClickListener true
        }

        binding.tgFilter.addOnButtonCheckedListener { _, checkedId, _ ->
            roomType = when (checkedId) {
                R.id.btn_filter_space_left -> {
                    "아파트"
                }

                R.id.btn_filter_space_mid -> {
                    "오피스텔"
                }

                R.id.btn_filter_space_right -> {
                    "빌라"
                }

                else -> null
            }
        }


        binding.slFilterSpaceMaxValue.addOnChangeListener { _, value, _ ->
            maxValue = value.toString()
        }

        binding.slFilterSpaceMinValue.addOnChangeListener { _, value, _ ->
            minValue = value.toString()
        }


        chipViewModel.chipData.observe(viewLifecycleOwner) {
            it.split(",").forEach { condText ->
                val chip = Chip(context).apply {
                    text = condText
                    isCheckable = true
                }
                chip.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        condList.add(condText)
                    } else {
                        condList.remove(condText)
                    }
                }
                binding.cgFilter.addView(chip)
            }
        }


    }
}