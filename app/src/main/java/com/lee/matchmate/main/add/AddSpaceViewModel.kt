package com.lee.matchmate.main.add

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddSpaceViewModel : ViewModel() {
    var pImageData: MutableLiveData<Uri?> = MutableLiveData()
    var aImageData: MutableLiveData<List<Uri>?> = MutableLiveData()
    var spaceSelectedCondList: MutableLiveData<MutableSet<String>> = MutableLiveData()
}