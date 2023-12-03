package com.lee.matchmate.common

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.kakao.sdk.common.KakaoSdk
import com.lee.matchmate.BuildConfig

class AppGlobalContext : Application() {

    override fun onCreate() {

        super.onCreate()
        prefs = this.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        KakaoSdk.init(this,BuildConfig.KAKAO_API_KEY)
        appContext = this
    }
    companion object {
        private  var appContext: AppGlobalContext? = null
        lateinit var prefs : SharedPreferences


        fun getAppContext() : AppGlobalContext{
            return  appContext  ?: run { return AppGlobalContext() }
        }
    }
}