package com.lee.matchmate.main

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.lee.matchmate.R
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentMainBinding
import com.lee.matchmate.main.decoration.MainDecoration
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainFragment : ViewBindingBaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate),
    OnMapReadyCallback {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var mMap: GoogleMap

    companion object {
        fun newInstance() = MainFragment()
    }

    private val compositeDisposable = CompositeDisposable()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cityArray = resources.getStringArray(R.array.arr_city)
        val cityArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_city_item, cityArray)

        val districtArray = resources.getStringArray(R.array.arr_district)
        val districtArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_district_item, districtArray)

        /**
         * Map fragment
         *
         */
        val mapFragment = childFragmentManager.findFragmentById(R.id.main_map) as SupportMapFragment
        mapFragment.getMapAsync(this@MainFragment)


        with(binding) {
            tvCityDropdown.setAdapter(cityArrayAdapter)
            val adapter = MainAdapter(viewModel.spaceData.value)
            rvMainSpace.adapter = adapter
            rvMainSpace.layoutManager = LinearLayoutManager(context)
            rvMainSpace.addItemDecoration(MainDecoration(0, R.color.lightGrey, 20))

            viewModel.spaceData.observe(viewLifecycleOwner) {
                it.let {
                    adapter.submitList(it)
                }
            }

            tbMain.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.menu_main_space) {
                    val action = MainFragmentDirections.actionMainFragmentToAddSpaceFragment()
                    findNavController().navigate(action)
                    return@setOnMenuItemClickListener true
                }
                false
            }

            btnMainFilter.setOnClickListener {
                val action = MainFragmentDirections.actionMainFragmentToFilterFragment()
                findNavController().navigate(action)
            }

        }

        binding.tvCityDropdown.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> {
                    binding.tlDistrictDropdown.isEnabled = true
                    binding.tvDistrictDropdown.setAdapter(districtArrayAdapter)
                    setLocation(37.5665, 126.9780)

                }

                else -> binding.tlDistrictDropdown.isEnabled = false
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    private fun setLocation(latitude: Double, longitude: Double) {
        val latlng = LatLng(latitude, longitude)
        val cameraPosition = CameraPosition.Builder().target(latlng).zoom(15.0f).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latLng = LatLng(37.566168, 126.901609)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f))
    }
}