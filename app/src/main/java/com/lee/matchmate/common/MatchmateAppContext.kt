package com.lee.matchmate.common

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.kakao.sdk.common.KakaoSdk
import com.lee.matchmate.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MatchmateAppContext : Application() {

    override fun onCreate() {

        super.onCreate()
        prefs = this.getSharedPreferences(Constants.SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        appContext = this
    }

    companion object {
        private lateinit var appContext: MatchmateAppContext
        lateinit var prefs: SharedPreferences
        fun getAppContext() = appContext
    }
}