package com.example.cowboysstore.di

import android.content.Context
import com.example.cowboysstore.data.local.prefs.Preferences
import com.example.cowboysstore.data.local.prefs.PreferencesImpl
import com.example.cowboysstore.data.remote.AcceptInterceptor
import com.example.cowboysstore.data.remote.AuthorizationInterceptor
import com.example.cowboysstore.data.remote.RemoteApi
import com.example.cowboysstore.data.repository.RemoteRepository
import com.example.cowboysstore.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun providePreferences(
        @ApplicationContext context: Context
    ) : Preferences = PreferencesImpl(context)

    @Provides
    @Singleton
    fun provideOkHttpClient(preferences: Preferences): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(AcceptInterceptor())
            .addInterceptor(AuthorizationInterceptor(preferences))
            .addInterceptor(HttpLoggingInterceptor().apply {// Logging
                HttpLoggingInterceptor.Level.BODY
            })
            .build()
    
    @Provides
    @Singleton
    fun provideRemoteApi(okHttpClient: OkHttpClient): RemoteApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RemoteApi::class.java)
}

