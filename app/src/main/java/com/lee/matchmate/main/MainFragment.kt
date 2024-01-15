package com.lee.matchmate.main

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import com.lee.matchmate.BuildConfig
import com.lee.matchmate.R
import com.lee.matchmate.common.Constants
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentMainBinding
import com.lee.matchmate.main.add.AddSpaceViewModel
import com.lee.matchmate.main.decoration.MainDecoration
import com.lee.matchmate.main.geocoder.GeocoderViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainFragment : ViewBindingBaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate),
    OnMapReadyCallback {

    private val viewModel: MainViewModel by activityViewModels()
    private val addSpaceViewModel: AddSpaceViewModel by activityViewModels()
    private val geoViewModel: GeocoderViewModel by viewModels()
    private lateinit var mMap: GoogleMap
    private var isMapReady = false


    companion object {
        fun newInstance() = MainFragment()
    }

    private val compositeDisposable = CompositeDisposable()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * ViewModel 값 초기화
         */
        with(addSpaceViewModel) {
            pImageData.value = Uri.EMPTY
            aImageData.value = listOf()
            spaceSelectedCondList.value = mutableSetOf()
            markerPosition.value = LatLng(
                37.566168,
                126.901609
            )
        }


        /**
         * DropDownMenu 초기화
         */
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


        /**
         * Marker 추가
         */
        viewModel.spaceMarker.observe(viewLifecycleOwner) {
            if (isMapReady) {
                mMap.clear()

                it.forEach { spaceMarker ->
                    if (spaceMarker.lat.isNotBlank() && spaceMarker.lng.isNotBlank()) {
                        val latlng =
                            LatLng(spaceMarker.lat.toDouble(), spaceMarker.lng.toDouble())
                        mMap.addMarker(MarkerOptions().position(latlng))
                    }

                }
            }

        }



        with(binding) {
            tvCityDropdown.setAdapter(cityArrayAdapter)
            val adapter = MainAdapter(viewModel,lifecycleScope)
            rvMainSpace.adapter = adapter
            rvMainSpace.layoutManager = LinearLayoutManager(context)
            rvMainSpace.addItemDecoration(MainDecoration(0, R.color.lightGrey, 20))


            viewModel.filteredData.observe(viewLifecycleOwner) { filteredList ->
                adapter.submitList(filteredList)
                binding.cgMain.removeAllViews()
                viewModel.selectedCondList.value?.forEach { condText ->
                    val chip = Chip(context).apply {
                        text = condText
                        isCloseIconVisible = true
                    }
                    chip.setOnCloseIconClickListener {
                        val chipText = chip.text.toString()
                        val newList = viewModel.selectedCondList.value?.toMutableList()
                        newList?.remove(chipText)
                        viewModel.selectedCondList.postValue(newList)
                        viewModel.filterData()
                    }
                    binding.cgMain.addView(chip)
                }

            }



            viewModel.spaceData.observe(viewLifecycleOwner) {
                it.let {
                    viewModel.filterData()
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

            binding.tvCityDropdown.setOnItemClickListener { parent, view, position, id ->
                val selectedCity = parent.getItemAtPosition(position).toString()
                viewModel.selectedCity.postValue(selectedCity)

                when (position) {
                    0 -> {
                        binding.tlDistrictDropdown.isEnabled = true
                        binding.tvDistrictDropdown.setAdapter(districtArrayAdapter)
                        setLocation(Constants.MAIN_LAT, Constants.MAIN_LNG)

                    }

                    else -> binding.tlDistrictDropdown.isEnabled = false
                }
            }

            binding.tvDistrictDropdown.setOnItemClickListener { parent, view, position, id ->
                val selectedDistrict = parent.getItemAtPosition(position).toString()
                val selectedCity = binding.tvCityDropdown.text.toString()
                viewModel.selectedDistrict.postValue(selectedCity)
                val address =
                    "${Constants.LOCALITY_PREFIX}$selectedCity $selectedDistrict|${Constants.COUNTRY_PREFIX}${Constants.COUNTRY_CODE}"

                geoViewModel.getGeoCode(address, BuildConfig.MAPS_API_KEY)
            }
        }



        geoViewModel.getGeoEntityResponseLiveData().observe(viewLifecycleOwner) {
            if (it != null && it.results.isNotEmpty()) {
                val location = it.results[0].geometry.location
                setLocation(location.lat, location.lng)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    private fun setLocation(latitude: Double, longitude: Double) {
        if (isMapReady) {
            val latlng = LatLng(latitude, longitude)
            val cameraPosition = CameraPosition.Builder().target(latlng).zoom(13.0f).build()
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        isMapReady = true
        val latLng = LatLng(Constants.LAT, Constants.LNG)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constants.ZOOM_LEVEL))

        viewModel.spaceMarker.value?.forEach { spaceMarker ->
            if (spaceMarker.lat.isNotBlank() && spaceMarker.lng.isNotBlank()) {
                val latlng =
                    LatLng(spaceMarker.lat.toDouble(), spaceMarker.lng.toDouble())
                mMap.addMarker(MarkerOptions().position(latlng))
            }
        }
    }
}