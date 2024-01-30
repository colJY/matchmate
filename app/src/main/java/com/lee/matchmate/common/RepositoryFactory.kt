package com.lee.matchmate.common

import com.lee.matchmate.chat.detail.ChatDetailRepository
import com.lee.matchmate.chat.fcm.FCMRepository
import com.lee.matchmate.favorite.FavoriteRepository
import com.lee.matchmate.main.MainRepository
import com.lee.matchmate.main.detail.DetailRepository
import com.lee.matchmate.main.geocoder.GeocoderRepository
import com.lee.matchmate.main.selectchip.ChipSelectRepository

class RepositoryFactory {
    companion object {

        val chatDetailRepository: ChatDetailRepository by lazy {
            ChatDetailRepository()
        }

        val fcmRepository: FCMRepository by lazy {
            FCMRepository()
        }

        val favoriteRepository: FavoriteRepository by lazy {
            FavoriteRepository()
        }

        val detailRepository: DetailRepository by lazy {
            DetailRepository()
        }

        val geocoderRepository: GeocoderRepository by lazy {
            GeocoderRepository()
        }

        val chipSelectRepository: ChipSelectRepository by lazy {
            ChipSelectRepository()
        }

        val mainRepository: MainRepository by lazy {
            MainRepository()
        }

    }
}
