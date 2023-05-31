package com.example.cowboysstore.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.cowboysstore.R
import com.example.cowboysstore.data.model.AuthRequest
import com.example.cowboysstore.data.model.ProfileChanges
import com.example.cowboysstore.data.remote.RemoteApi
import com.example.cowboysstore.domain.entities.Order
import com.example.cowboysstore.domain.entities.OrderRequestModel
import com.example.cowboysstore.domain.entities.Product
import com.example.cowboysstore.domain.entities.Profile
import com.example.cowboysstore.domain.repository.RemoteRepository
import com.example.cowboysstore.utils.GsonErrorParser
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val remoteApiClient: RemoteApi
) : RemoteRepository {

    override suspend fun getProducts(): Result<List<Product>> {
        try {
            val response = remoteApiClient.getProducts()
            return if (response.isSuccessful) {
                Result.success(response.body()?.productsList ?: emptyList())
            } else {
                return Result.failure(LoadException(R.string.catalog_notice_error, R.string.catalog_notice_error_message))
            }
        } catch (e: Exception) {
            return Result.failure(LoadException(R.string.unknown_error, R.string.unknown_error_message))
        }
    }

    override suspend fun getProfile(): Result<Profile> {
        try {
            val response = remoteApiClient.getProfile()
            return if (response.isSuccessful) {
                Result.success(response.body()?.responseData?.profile ?: Profile())
            } else {
                return Result.failure(LoadException(R.string.profile_data_loading_error, R.string.profile_data_loading_message))
            }
        } catch (e: Exception) {
            return Result.failure(LoadException(R.string.unknown_error, R.string.unknown_error_message))
        }
    }

    override suspend fun signIn(email: String, password: String): Result<String> {
        return try {
            val response = remoteApiClient.signIn(AuthRequest(email, password))
            if (response.isSuccessful) {
                Result.success(response.body()?.responseBody?.accessToken ?: "")
            } else {
                val errorMessage = GsonErrorParser.parseError(response.errorBody()?.string())
                Result.failure(LoadException(errorMessage = errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(LoadException(R.string.unknown_error, R.string.unknown_error_message))
        }
    }


    override suspend fun getProductDetailsById(id: String): Result<Product> {
        try {
            val response = remoteApiClient.getProductById(id)
            return if (response.isSuccessful) {
                val product = response.body()?.product ?: Product()
                /* If wrong request product id */
                if (product.id.isEmpty()) {
                    Result.failure(LoadException())
                } else {
                    Result.success(product)
                }
            } else {
                Result.failure(LoadException(R.string.product_error_text, R.string.unknown_error_message))
            }
        } catch (e: Exception) {
            return Result.failure(LoadException(R.string.unknown_error, R.string.unknown_error_message))
        }
    }

    override suspend fun getOrders(): Result<List<Order>> {
        try {
            val response = remoteApiClient.getOrders()
            return if (response.isSuccessful) {
                val orders = response.body()?.orders ?: emptyList()
                /* If no orders exists */
                if (orders.isEmpty()) {
                    Result.failure(LoadException(R.string.orders_no_data, R.string.orders_no_data_message))
                } else {
                    Result.success(orders)
                }
            } else {
                Result.failure(LoadException())
            }
        } catch (e: Exception) {
            return Result.failure(LoadException(R.string.unknown_error, R.string.unknown_error_message))
        }
    }

    override suspend fun cancelOrder(orderId: String): Result<Order> {
        return try {
            val response = remoteApiClient.cancelOrder(orderId)
            if (response.isSuccessful) {
                val order = response.body()?.order ?: Order()
                if (order == Order()) {
                    Result.failure(LoadException())
                } else {
                    Result.success(order)
                }
            } else {
                Result.failure(LoadException())
            }
        } catch (e: Exception) {
            Result.failure(LoadException(R.string.unknown_error, R.string.unknown_error_message))
        }
    }

    override suspend fun changeProfile(changedFields: List<ProfileChanges>): Result<Boolean> {
        return try {
            val response = remoteApiClient.changeProfile(changedFields)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(LoadException(R.string.settings_change_error))
            }
        } catch (e: Exception) {
            Result.failure(LoadException(R.string.unknown_error, R.string.unknown_error_message))
        }
    }

    override suspend fun createOrder(orderRequestModel: OrderRequestModel): Result<Int> {
        return try {
            val response = remoteApiClient.createOrder(orderRequestModel)
            if (response.isSuccessful) {
                val orderNumber = response.body()?.data?.number ?: 0
                Result.success(orderNumber)
            } else {
                Result.failure(LoadException(R.string.checkout_error_message))
            }
        } catch (e: Exception) {
            Result.failure(LoadException(R.string.checkout_error_message))
        }
    }

    override suspend fun getUserPhoto(id: String): Result<Bitmap> {
        return try {
            val bitMapImg = BitmapFactory.decodeStream(remoteApiClient.getUserPhoto(id).body()?.byteStream())
            if (bitMapImg == null) {
                Result.failure(LoadException())
            } else {
                Result.success(bitMapImg)
            }
        } catch (e: Exception) {
            Result.failure(LoadException())
        }
    }
}