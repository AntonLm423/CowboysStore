package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.BuildConfig

class GetAppVersionUseCase {

     fun getAppVersion() = "${BuildConfig.VERSION_CODE}"

    }
