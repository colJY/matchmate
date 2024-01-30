package com.lee.matchmate.main.add

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.jakewharton.rxbinding4.view.clicks
import com.lee.matchmate.BuildConfig
import com.lee.matchmate.R
import com.lee.matchmate.common.MatchmateAppContext
import com.lee.matchmate.common.Constants
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentAddSpaceBinding
import com.lee.matchmate.main.FireSpace
import com.lee.matchmate.main.MainViewModel
import com.lee.matchmate.main.User
import com.lee.matchmate.main.geocoder.GeocoderViewModel
import com.lee.matchmate.main.geocoder.ReverseGeoEntity
import com.lee.matchmate.main.geocoder.SpaceMarker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class AddSpaceFragment :
    ViewBindingBaseFragment<FragmentAddSpaceBinding>(FragmentAddSpaceBinding::inflate),
    OnMapReadyCallback {
    private val viewModel: AddSpaceViewModel by activityViewModels()
    private val geoViewModel: GeocoderViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = Constants.FIRESTORE_COLLECTION_NAME
    private val fireStoreLatLngCollectionName = Constants.LATLNG_COLLECTION_NAME
    private val fireStoreUserCollectionName = Constants.USER_COLLECTION_NAME
    private lateinit var mMap: GoogleMap
    private val spaceImageAdapter = AddSpaceImageAdapter()
    private var isMapReady = false


    companion object {
        fun newInstance() = AddSpaceFragment()
    }


    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val mapFragment = childFragmentManager.findFragmentById(R.id.add_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        super.onViewCreated(view, savedInstanceState)
        val fireSpace = FireSpace()

        lifecycleScope.launch {
            if (isMapReady) {
                viewModel.markerPosition.collect { newPosition ->
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 16.0f))
                }
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.pImageData.observe(viewLifecycleOwner) {
                    if (it != null && it != Uri.EMPTY) {
                        binding.ivAddPrimaryImage.setImageURI(it)
                        binding.ivAddPrimaryImage.background = null
                        fireSpace.primaryImage = it.toString()
                    }
                }
                viewModel.aImageData.observe(viewLifecycleOwner) {
                    if (!it.isNullOrEmpty()) {
                        spaceImageAdapter.submitList(it)
                        binding.ivAddAdditionalImage.background = null
                        fireSpace.additionalImage = it.toString()
                    }
                }
            }
        }


        with(binding) {
            tbAdd.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.menu_filter_check) {
                    fireSpace.value = slAddValue.value.toString()
                    fireSpace.title = edAdd.text.toString()
                    fireSpace.userId =
                        MatchmateAppContext.prefs.getString(Constants.USER_ID, Constants.BLANK)
                            .toString()


                    val latLng = mMap.cameraPosition.target
                    val spaceMarker =
                        SpaceMarker(latLng.latitude.toString(), latLng.longitude.toString())

                    insertLatLngFireStore(spaceMarker)

                    val address = "${latLng.latitude},${latLng.longitude}"

                    geoViewModel.getReverseGeoCode(
                        latlng = address,
                        apiKey = BuildConfig.MAPS_API_KEY
                    )

                    uploadFireStorage(viewModel.pImageData.value.toString())
                    viewModel.aImageData.value?.forEach { uri ->
                        uploadFireStorage(uri.toString())
                    }

                    val reverseGeoEntity: ReverseGeoEntity? =
                        geoViewModel.getReverseGeoEntityResponseLiveData().value
                    val action =
                        AddSpaceFragmentDirections.actionAddSpaceFragmentToMainFragment()

                    geoViewModel.getReverseGeoEntityResponseLiveData().observe(viewLifecycleOwner) {
                        fireSpace.location = it.results[0].formattedAddress
                        insertFireStore(fireSpace)
                        if (it != null) {
                            findNavController().navigate(action)
                        } else {
                            findNavController().navigate(action)
                        }
                    }


                    return@setOnMenuItemClickListener true
                }
                false
            }



            chAdd.clicks().observeOn(Schedulers.io())
                .throttleFirst(Constants.THROTTLE_FIRST_INTERVAL, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val action =
                        AddSpaceFragmentDirections.actionAddSpaceFragmentToChipSelectFragment()
                    findNavController().navigate(action)
                }

            val getImageAction =
                registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                    viewModel.pImageData.postValue(uri)
                    fireSpace.primaryImage = uri.toString()
                }


            rvAddAdditionalImage.adapter = spaceImageAdapter
            rvAddAdditionalImage.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            val getAddImageAction =
                registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
                    spaceImageAdapter.submitList(uriList)
                    viewModel.aImageData.postValue(uriList)
                    fireSpace.additionalImage = uriList.toString()
                }


            ivAddPrimaryImage
                .clicks()
                .observeOn(Schedulers.io())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    getImageAction.launch(Constants.IMAGE_PICKER)
                }

            ivAddAdditionalImage
                .clicks()
                .observeOn(Schedulers.io())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    getAddImageAction.launch(Constants.IMAGE_PICKER)
                }

            viewModel.spaceSelectedCondList.observe(viewLifecycleOwner) {

                fireSpace.cond =
                    it.toString().replace(Constants.SQUARE_BRACKET_LEFT, Constants.BLANK)
                        ?.replace(Constants.SQUARE_BRACKET_RIGHT, Constants.BLANK).toString()
                cgAddCond.removeView(chAdd)
                it.forEach { chipText ->
                    cgAddCond.addView(Chip(context).apply {
                        text = chipText
                    })
                }
                cgAddCond.addView(chAdd)
            }

            tgAddFilter.addOnButtonCheckedListener { group, checkedId, isChecked ->
                when (checkedId) {
                    R.id.btn_add_filter_space_left -> {
                        fireSpace.type = btnAddFilterSpaceLeft.text.toString()
                    }

                    R.id.btn_add_filter_space_mid -> {
                        fireSpace.type = btnAddFilterSpaceMid.text.toString()
                    }

                    R.id.btn_add_filter_space_right -> {
                        fireSpace.type = btnAddFilterSpaceRight.text.toString()
                    }


                    else -> {
                        fireSpace.type = Constants.BLANK
                    }
                }
            }

        }

    }

    private fun insertFireStore(fireSpace: FireSpace) {
        val documentRef = fireStoreDB.collection(fireStoreCollectionName)
        documentRef.add(fireSpace.toMap()).addOnSuccessListener {
            val generatedId = it.id
            insertUserIDFireStore(generatedId)
        }.addOnFailureListener {
        }
    }

    private fun insertLatLngFireStore(spaceMarker: SpaceMarker) {
        val documentRef = fireStoreDB.collection(fireStoreLatLngCollectionName)
        documentRef.add(spaceMarker.toMap()).addOnSuccessListener {
        }.addOnFailureListener {
        }
    }

    private fun insertUserIDFireStore(spaceId: String) {
        val documentRef = fireStoreDB.collection(fireStoreUserCollectionName)
            .document(
                MatchmateAppContext.prefs.getString(Constants.USER_ID, Constants.BLANK).toString()
            )
        documentRef.get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            if (it.exists()) {
                if (user != null) {
                    user.spaceId.add(spaceId)
                    documentRef.update(user.toMap())
                }

            }
        }
    }


    private fun uploadFireStorage(fromPath: String) {
        val fromUri = Uri.parse(fromPath)
        val storageRef = FirebaseStorage.getInstance().reference
        val uploadTask = storageRef.child(Constants.UPLOAD_IMAGES_URI + fromUri.lastPathSegment)
            .putStream(requireContext().contentResolver.openInputStream(fromUri)!!)

        uploadTask.addOnFailureListener {
            // 실패 시 처리
            it.printStackTrace()

        }.addOnSuccessListener {
            mainViewModel.isSuccess.value = false
            mainViewModel.isSuccess.value = true

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        isMapReady = true
        mMap = googleMap

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viewModel.markerPosition.value, 16.0f))
        val marker = mMap.addMarker(
            MarkerOptions().position(mMap.cameraPosition.target).draggable(false)
        )

        mMap.setOnCameraMoveListener {
            marker!!.position = mMap.cameraPosition.target
            lifecycleScope.launch {
                viewModel.markerPosition.emit(mMap.cameraPosition.target)
            }
        }


    }

}