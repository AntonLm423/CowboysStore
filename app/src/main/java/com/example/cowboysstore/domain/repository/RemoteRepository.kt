package com.example.cowboysstore.domain.repository

import android.graphics.Bitmap
import com.example.cowboysstore.data.model.ProfileChanges
import com.example.cowboysstore.domain.entities.Order
import com.example.cowboysstore.domain.entities.OrderRequestModel
import com.example.cowboysstore.domain.entities.Product
import com.example.cowboysstore.domain.entities.Profile

interface RemoteRepository {

    suspend fun getProducts(): Result<List<Product>>

    suspend fun getProfile(): Result<Profile>

    suspend fun signIn(email: String, password: String): Result<String>

    suspend fun getProductDetailsById(id: String): Result<Product>

    suspend fun getOrders(): Result<List<Order>>

    suspend fun cancelOrder(orderId: String): Result<Order>

    suspend fun changeProfile(changedFields: List<ProfileChanges>): Result<Boolean>

    suspend fun createOrder(orderRequestModel: OrderRequestModel) : Result<Int>

    suspend fun getUserPhoto(id : String) : Result<Bitmap>
}