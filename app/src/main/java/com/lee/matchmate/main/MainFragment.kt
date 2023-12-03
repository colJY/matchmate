package com.lee.matchmate.main

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.chip.Chip
import com.google.firebase.messaging.FirebaseMessaging
import com.lee.matchmate.BuildConfig
import com.lee.matchmate.R
import com.lee.matchmate.chat.fcm.TAG
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentMainBinding
import com.lee.matchmate.main.decoration.MainDecoration
import com.lee.matchmate.main.geocoder.GeocoderViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class MainFragment : ViewBindingBaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate),
    OnMapReadyCallback {

    private val viewModel: MainViewModel by activityViewModels()
    private val geoViewModel: GeocoderViewModel by viewModels()
    private lateinit var mMap: GoogleMap
    private var isMapReady = false


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


        //val geocoder = Geocoder(requireContext())

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
            if (!task.isSuccessful) {
                Log.e(TAG, "Fetching FCM registration token failed", task.exception)

            }
            val fcmToken = task.result
            Log.e(TAG, fcmToken)


        }


        /**
         * Map fragment
         *
         */
        val mapFragment = childFragmentManager.findFragmentById(R.id.main_map) as SupportMapFragment
        mapFragment.getMapAsync(this@MainFragment)


        viewModel.spaceMarker.observe(viewLifecycleOwner) {
            if (isMapReady) {
                mMap.clear()

                it.forEach { spaceMarker ->
                    if (spaceMarker != null) {
                        if (spaceMarker.lat.isNotBlank() && spaceMarker.lng.isNotBlank()) {
                            val latlng =
                                LatLng(spaceMarker.lat.toDouble(), spaceMarker.lng.toDouble())
                            mMap.addMarker(MarkerOptions().position(latlng))
                        }
                    }

                }
            }

        }

        viewModel.selectedMaxValue.observe(viewLifecycleOwner){
            Log.e("asd1111",it)
        }


        with(binding) {
            tvCityDropdown.setAdapter(cityArrayAdapter)
            val adapter = MainAdapter(viewModel)
//            val adapter = MainAdapter(viewModel.spaceData.value,viewModel)
            rvMainSpace.adapter = adapter
            rvMainSpace.layoutManager = LinearLayoutManager(context)
            rvMainSpace.addItemDecoration(MainDecoration(0, R.color.lightGrey, 20))


            viewModel.filteredData.observe(viewLifecycleOwner) { filteredList ->
                adapter.submitList(filteredList)
                Log.d("MainFragment", "FilteredData observed: $filteredList")
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

        }

        binding.tvCityDropdown.setOnItemClickListener { parent, view, position, id ->
            val selectedCity = parent.getItemAtPosition(position).toString()
            viewModel.selectedCity.postValue(selectedCity)

            when (position) {
                0 -> {
                    binding.tlDistrictDropdown.isEnabled = true
                    binding.tvDistrictDropdown.setAdapter(districtArrayAdapter)
                    setLocation(37.5665, 126.9780)

                }

                else -> binding.tlDistrictDropdown.isEnabled = false
            }
        }

        binding.tvDistrictDropdown.setOnItemClickListener { parent, view, position, id ->
            val selectedDistrict = parent.getItemAtPosition(position).toString()
            val selectedCity = binding.tvCityDropdown.text.toString()
            viewModel.selectedDistrict.postValue(selectedCity)
            val address = "locality:$selectedCity $selectedDistrict|country:KR"

            geoViewModel.getGeoCode(address, BuildConfig.MAPS_API_KEY)
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
        val latLng = LatLng(37.566168, 126.901609)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f))
    }
}