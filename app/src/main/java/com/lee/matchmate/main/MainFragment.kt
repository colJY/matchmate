package com.lee.matchmate.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.matchmate.R
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentMainBinding
import com.lee.matchmate.main.decoration.MainDecoration
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainFragment : ViewBindingBaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel : MainViewModel by viewModels()
    companion object {
        fun newInstance() = MainFragment()
    }

    private val compositeDisposable = CompositeDisposable()

    val space1 = Space("asd","asd","asd")
    val space2 = Space("zxc","zxc","zxc")
    val space3 = Space("qwe","qwe","Qwe")
    val space4 = Space("aaa","aaa","aaa")
    val space5 = Space("eee","eee","eee")
    private val spaceData = arrayListOf<Space>(space1,space2,space3,space4,space5)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cityArray = resources.getStringArray(R.array.arr_city)
        val cityArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_city_item, cityArray)

        val districtArray = resources.getStringArray(R.array.arr_district)
        val districtArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_district_item,districtArray)


        with(binding){
            binding.tvCityDropdown.setAdapter(cityArrayAdapter)

            rvMainSpace.layoutManager = LinearLayoutManager(context)
            rvMainSpace.addItemDecoration(MainDecoration(0, R.color.lightGrey,20))

            rvMainSpace.adapter = MainAdapter(spaceData)
        }

        binding.tvCityDropdown.setOnItemClickListener { parent, view, position, id ->
            when(position){
                0 -> {
                    binding.tlDistrictDropdown.isEnabled = true
                    binding.tvDistrictDropdown.setAdapter(districtArrayAdapter)
                }
                else-> binding.tlDistrictDropdown.isEnabled = false
            }
        }

    }
}