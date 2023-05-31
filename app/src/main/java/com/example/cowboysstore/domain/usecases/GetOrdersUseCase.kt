package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.domain.entities.Order
import com.example.cowboysstore.domain.repository.RemoteRepository
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun getOrders(): List<Order> {
        val result = remoteRepository.getOrders()
        return result.getOrElse {
            throw result.exceptionOrNull() ?: LoadException()
        }
    }

}