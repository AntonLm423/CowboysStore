package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.domain.entities.OrderRequestModel
import com.example.cowboysstore.domain.repository.RemoteRepository
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun createOrder(orderRequestModel: OrderRequestModel) : Int {
        return remoteRepository.createOrder(orderRequestModel).getOrElse {
            throw LoadException()
        }
    }

}