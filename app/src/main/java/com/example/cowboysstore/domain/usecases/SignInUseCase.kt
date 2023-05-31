package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.domain.repository.RemoteRepository
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun signIn(email: String, password: String): String {
        val result = remoteRepository.signIn(email, password)
        return result.getOrElse {
            throw result.exceptionOrNull() ?: LoadException()
        }
    }
}