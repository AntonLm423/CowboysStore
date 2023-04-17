package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.data.repository.RemoteRepository
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class GetAppVersionUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun getAppVersion() : String {
        return remoteRepository.getAppVersion().getOrElse {
            throw LoadException()
        }
    }

}