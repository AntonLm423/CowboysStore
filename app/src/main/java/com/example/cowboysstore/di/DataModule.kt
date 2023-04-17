package com.example.cowboysstore.di

import com.example.cowboysstore.data.remote.RemoteApi
import com.example.cowboysstore.data.repository.RemoteRepository
import com.example.cowboysstore.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRemoteRepository(apiClient: RemoteApi): RemoteRepository =
        RemoteRepository(apiClient)


    @Provides
    @Singleton
    fun provideRemoteApi(): RemoteApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RemoteApi::class.java)
}

