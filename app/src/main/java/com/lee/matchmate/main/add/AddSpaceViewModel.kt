package com.lee.matchmate.main.add

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.lee.matchmate.common.Constants

import kotlinx.coroutines.flow.MutableStateFlow

class AddSpaceViewModel : ViewModel() {
    var pImageData: MutableLiveData<Uri?> = MutableLiveData()
    var aImageData: MutableLiveData<List<Uri>?> = MutableLiveData()
    var spaceSelectedCondList: MutableLiveData<MutableSet<String>> = MutableLiveData()

    var markerPosition = MutableStateFlow(
        LatLng(
            Constants.LAT,
            Constants.LNG
        )
    )
}

