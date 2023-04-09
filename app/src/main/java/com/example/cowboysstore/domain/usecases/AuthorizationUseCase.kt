package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.data.repository.MockRepository
import com.example.cowboysstore.presentation.utils.LoadException
import javax.inject.Inject

class AuthorizationUseCase @Inject constructor(
    private val mockRepository: MockRepository
) {

    suspend fun authorizationUseCase(
        email : String,
        password : String
    ) : Boolean {
        return mockRepository.authorization(email, password).getOrElse {
            throw LoadException()
        }
    }


}