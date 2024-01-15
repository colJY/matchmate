package com.lee.matchmate.main.filter

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.lee.matchmate.R
import com.lee.matchmate.common.Constants
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

        var maxValue = Constants.BLANK
        var minValue = Constants.BLANK
        val condList: MutableList<String> = mutableListOf()
        var roomType: String? = null
        var selectedDistrictValue = Constants.BLANK

        val cityArray = resources.getStringArray(R.array.arr_city)
        val cityArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_city_item, cityArray)

        val districtArray = resources.getStringArray(R.array.arr_district)
        val districtArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_district_item, districtArray)


        with(viewModel) {
            selectedType.value = Constants.BLANK
            selectedMaxValue.value = Constants.BLANK
            selectedMinValue.value = Constants.BLANK
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
                selectedDistrict.postValue(selectedDistrictValue)
                selectedType.postValue(roomType)
                filterData()
            }


            findNavController().navigate(action)
            return@setOnMenuItemClickListener true
        }

        binding.tgFilter.addOnButtonCheckedListener { _, checkedId, _ ->
            roomType = when (checkedId) {
                R.id.btn_filter_space_left -> {
                    Constants.roomApart
                }

                R.id.btn_filter_space_mid -> {
                    Constants.roomOffice
                }

                R.id.btn_filter_space_right -> {
                    Constants.roomVilla
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

        binding.tvCityDropdown.setAdapter(cityArrayAdapter)
        binding.tvFilterDistrictDropdown.setAdapter(districtArrayAdapter)
        binding.tvCityDropdown.setOnItemClickListener { parent, view, position, id ->
            val selectedCity = parent.getItemAtPosition(position).toString()

            when (position) {
                0 -> {
                    binding.tvFilterDistrictDropdown.isEnabled = true

                }

                else -> binding.tvFilterDistrictDropdown.isEnabled = false
            }
        }

        binding.tvFilterDistrictDropdown.setOnItemClickListener { parent, view, position, id ->
            selectedDistrictValue = parent.getItemAtPosition(position).toString()
        }


        chipViewModel.chipData.observe(viewLifecycleOwner) {
            it.split(Constants.SPLIT).forEach { condText ->
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