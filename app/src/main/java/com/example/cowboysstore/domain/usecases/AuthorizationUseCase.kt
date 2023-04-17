package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.data.repository.RemoteRepository
import com.example.cowboysstore.utils.AuthException
import javax.inject.Inject

class AuthorizationUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun authorizationUseCase(
        email: String,
        password: String
    ): String {

        val result = remoteRepository.authorization(email,password)

        return result.getOrElse {
            throw AuthException(result.exceptionOrNull()?.message ?: "Invalid Error")
        }

    }
}