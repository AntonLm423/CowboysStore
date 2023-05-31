package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.domain.entities.Product
import com.example.cowboysstore.domain.repository.RemoteRepository
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun getProductById(id: String): Product {
        val result = remoteRepository.getProductDetailsById(id)
        return result.getOrElse {
            throw result.exceptionOrNull() ?: LoadException()
        }
    }
}