package com.lee.matchmate.common

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.lee.matchmate.BuildConfig

class AppGlobalContext : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this,BuildConfig.KAKAO_API_KEY)
        appContext = this
    }
    companion object {
        private  var appContext: AppGlobalContext? = null
        fun getAppContext() : AppGlobalContext{
            return  appContext  ?: run { return AppGlobalContext() }
        }
    }
}