package com.example.cowboysstore.di

import com.example.cowboysstore.data.repository.MockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideMockRepository() : MockRepository = MockRepository()

}