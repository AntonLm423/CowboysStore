package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.data.repository.MockRepository
import com.example.cowboysstore.presentation.utils.LoadException
import javax.inject.Inject

class GetAppVersionUseCase @Inject constructor(
    private val mockRepository: MockRepository
) {

    suspend fun getAppVersion() : String {
        return mockRepository.getAppVersion().getOrElse {
            throw LoadException()
        }
    }

}