package com.lee.matchmate.main.add

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.jakewharton.rxbinding4.view.clicks
import com.lee.matchmate.R
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.common.toastMessage
import com.lee.matchmate.databinding.FragmentAddSpaceBinding
import com.lee.matchmate.main.FireSpace
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class AddSpaceFragment :
    ViewBindingBaseFragment<FragmentAddSpaceBinding>(FragmentAddSpaceBinding::inflate),
    OnMapReadyCallback {
    private val viewModel: AddSpaceViewModel by activityViewModels()

    private val job = CoroutineScope(Dispatchers.IO)
    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = "Space"
    private lateinit var mMap: GoogleMap
    private val spaceImageAdapter = AddSpaceImageAdapter()

    companion object {
        fun newInstance() = AddSpaceFragment()
    }


    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val mapFragment = childFragmentManager.findFragmentById(R.id.add_map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        super.onViewCreated(view, savedInstanceState)
        val fireSpace: FireSpace = FireSpace()



        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.pImageData.observe(viewLifecycleOwner) {
                    if (it != null) {
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
            tbAdd.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            tbAdd.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.menu_filter_check) {
                    fireSpace.value = slAddValue.value.toString()
                    val latLng = mMap.cameraPosition.target

                    //Retrofit (Google Maps Geocoding API 사용 예정)
                    fireSpace.location = "${latLng.latitude}:${latLng.longitude}"

                    insertFireStore(fireSpace)
                    uploadFireStorage(viewModel.pImageData.value.toString())


                    val action = AddSpaceFragmentDirections.actionAddSpaceFragmentToMainFragment()
                    findNavController().navigate(action)
                    return@setOnMenuItemClickListener true
                }
                false
            }

            chAdd.clicks().observeOn(Schedulers.io())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val action =
                        AddSpaceFragmentDirections.actionAddSpaceFragmentToChipSelectFragment()
                    findNavController().navigate(action)
                }

            val getImageAction = registerForActivityResult(
                ActivityResultContracts.GetContent(),
                ActivityResultCallback { uri ->
                    viewModel.pImageData.postValue(uri)
                    fireSpace.primaryImage = uri.toString()

                }
            )



            rvAddAdditionalImage.adapter = spaceImageAdapter
            rvAddAdditionalImage.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            val getAddImageAction = registerForActivityResult(
                ActivityResultContracts.GetMultipleContents(),
                ActivityResultCallback { uriList ->
                    spaceImageAdapter.submitList(uriList)
                    viewModel.aImageData.postValue(uriList)
                    fireSpace.additionalImage = uriList.toString()
                }
            )


            ivAddPrimaryImage
                .clicks()
                .observeOn(Schedulers.io())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    getImageAction.launch("image/*")
                }

            ivAddAdditionalImage
                .clicks()
                .observeOn(Schedulers.io())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    getAddImageAction.launch("image/*")
                }

            viewModel.spaceSelectedCondList.observe(viewLifecycleOwner) {

                fireSpace.cond = it.toString().replace("[", "")?.replace("]", "").toString()
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

                    R.id.btn_add_filter_space_etc -> {
                        fireSpace.type = btnAddFilterSpaceEtc.text.toString()
                    }

                    else -> {
                        fireSpace.type = ""
                    }
                }
            }

        }

    }

    private fun insertFireStore(fireSpace: FireSpace) {
        val documentRef = fireStoreDB.collection(fireStoreCollectionName)
        documentRef.add(fireSpace.toMap()).addOnSuccessListener {
            toastMessage("값을 추가했습니다", activity as Activity)
        }.addOnFailureListener {
            toastMessage("실패", activity as Activity)
        }
    }


    private fun uploadFireStorage(fromPath: String) {
        val fromUri = Uri.parse(fromPath)
        val storageRef = FirebaseStorage.getInstance().reference
        val uploadTask = storageRef.child("uploadImages/" + fromUri.lastPathSegment)
            .putStream(requireContext().contentResolver.openInputStream(fromUri)!!)

        uploadTask.addOnFailureListener {
            // 실패 시 처리
        }.addOnSuccessListener {
            toastMessage("업로드 되었습니다", activity as Activity)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val seoul = LatLng(37.566168, 126.901609)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 16.0f))

        val marker = mMap.addMarker(
            MarkerOptions().position(mMap.cameraPosition.target).draggable(false)
        )

        mMap.setOnCameraMoveListener {
            marker!!.position = mMap.cameraPosition.target
        }


    }

}