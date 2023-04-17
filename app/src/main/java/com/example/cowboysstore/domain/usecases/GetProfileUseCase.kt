package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.utils.LoadException
import com.example.cowboysstore.data.model.Profile
import com.example.cowboysstore.data.repository.RemoteRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun getProfileByToken(accessToken : String) : Profile {
       return remoteRepository.getProfileByToken(accessToken).getOrElse {
            throw LoadException()
        }
    }

}