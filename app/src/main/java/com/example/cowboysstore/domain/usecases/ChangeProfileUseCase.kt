package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.data.model.ProfileChanges
import com.example.cowboysstore.data.repository.RemoteRepository
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class ChangeProfileUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun changeProfile(changedFields: List<ProfileChanges>): Boolean {
        return remoteRepository.changeProfile(changedFields).getOrElse {
            throw LoadException()
        }
    }

}