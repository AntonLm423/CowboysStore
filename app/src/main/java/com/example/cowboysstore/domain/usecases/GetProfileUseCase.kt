package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.utils.LoadException
import com.example.cowboysstore.domain.entities.Profile
import com.example.cowboysstore.domain.repository.RemoteRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun getProfile(): Profile {
        val result = remoteRepository.getProfile()
        return result.getOrElse {
            throw result.exceptionOrNull() ?: LoadException()
        }
    }
}