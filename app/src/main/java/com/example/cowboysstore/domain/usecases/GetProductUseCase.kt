package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.data.model.Product
import com.example.cowboysstore.data.repository.RemoteRepository
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun getProductById(
        accessToken: String,
        id : String
    ) : Product {
        return remoteRepository.getProductDetailsById(accessToken, id).getOrElse {
            throw LoadException()
        }
    }

}