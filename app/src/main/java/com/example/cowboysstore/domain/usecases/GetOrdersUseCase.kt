package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.data.model.Order
import com.example.cowboysstore.data.repository.RemoteRepository
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
){

    suspend fun getOrdersByToken(accessToken : String) : List<Order> {
       return remoteRepository.getOrdersByToken(accessToken).getOrElse {
            throw LoadException()
        }
    }

}