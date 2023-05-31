package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.domain.entities.Order
import com.example.cowboysstore.domain.repository.RemoteRepository
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class CancelOrderUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun cancelOrder(orderId : String) : Order {
        return remoteRepository.cancelOrder(orderId).getOrElse {
            throw LoadException()
        }
    }

}