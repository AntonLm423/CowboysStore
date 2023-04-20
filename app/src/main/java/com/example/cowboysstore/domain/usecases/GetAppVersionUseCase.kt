package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.BuildConfig
import com.example.cowboysstore.data.repository.RemoteRepository
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class GetAppVersionUseCase {

     fun getAppVersion() = "${BuildConfig.VERSION_CODE}"

    }
