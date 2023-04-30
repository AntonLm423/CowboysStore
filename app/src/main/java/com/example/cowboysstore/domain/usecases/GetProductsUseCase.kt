package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.data.model.Product
import com.example.cowboysstore.data.repository.RemoteRepository
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun getProducts() : List<Product> {
      val result = remoteRepository.getProducts()

       return result.getOrElse {
            return result.getOrElse {
                throw result.exceptionOrNull() ?: LoadException()
            }
        }
    }
}