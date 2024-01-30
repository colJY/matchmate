package com.lee.matchmate.chat

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {
    @Provides
    fun provideChatRepository() : ChatRepository = ChatRepository()
}