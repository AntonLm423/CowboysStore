package com.example.cowboysstore.di

import com.example.cowboysstore.domain.usecases.GetAppVersionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetAppVersionUseCase() = GetAppVersionUseCase()
}