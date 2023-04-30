package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.utils.LoadException
import com.example.cowboysstore.data.model.Profile
import com.example.cowboysstore.data.repository.RemoteRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun getProfile() : Profile {

       val result = remoteRepository.getProfile()

       return result.getOrElse {
            throw result.exceptionOrNull() ?: LoadException()
        }
    }

}