package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.data.model.Order
import com.example.cowboysstore.data.remote.RemoteApi
import com.example.cowboysstore.data.repository.RemoteRepository
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class CancelOrderUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun CancelOrder(orderId : String) : Order {
        return remoteRepository.cancelOrder(orderId).getOrElse {
            throw LoadException()
        }
    }

}