package com.lee.matchmate.common

import android.app.Activity
import android.widget.Toast

fun toastMessage(message: String, owner: Activity){
    Toast.makeText(owner, message, Toast.LENGTH_SHORT).show()
}
