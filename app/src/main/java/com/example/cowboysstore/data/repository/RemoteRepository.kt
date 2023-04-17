package com.example.cowboysstore.data.repository

import com.example.cowboysstore.data.model.*
import com.example.cowboysstore.data.remote.RemoteApi
import com.example.cowboysstore.utils.AuthException
import kotlinx.coroutines.delay
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val remoteApiClient : RemoteApi
) {



    suspend fun getAppVersion(): Result<String> {
        randomDelay()
        return randomResult("1.0.0 (117)")
    }

    suspend fun getProducts(accessToken : String): Result<List<Product>> {
       val response = remoteApiClient.getProducts(accessToken)

        return if (response.isSuccessful) {
            Result.success(response.body()?.product ?: emptyList())
        }
        else {
            // TODO: Remake Exception Logic
            Result.failure(AuthException(response.errorBody()?.string() ?: "Invalid Error"))
        }
    }

    suspend fun getProfileByToken(accessToken: String) : Result<Profile> {
        val response = remoteApiClient.getProfileByToken(accessToken)

        return if (response.isSuccessful) {
            Result.success(response.body()?.responseData?.profile ?: Profile("", "", "", ""))
        }
        else {
            Result.failure(AuthException(response.errorBody()?.string() ?: "Invalid Error"))
        }
    }

    suspend fun authorization(email: String, password: String): Result<String> {

        val response = remoteApiClient.authorization(AuthRequest(email, password))

        return if (response.isSuccessful) {
            Result.success(response.body()?.responseBody?.accessToken ?: "Invalid token")

        } else {
            Result.failure(AuthException(response.errorBody()?.string() ?: "Invalid Error"))
        }
    }

    suspend fun getProductDetailsById(
        accessToken : String,
        id : String
    ) : Result<Product> {
        val response = remoteApiClient.getProductById(accessToken, id)

        return if (response.isSuccessful) {
            Result.success(response.body()?.product ?: Product())
        }
        else {
            Result.failure(AuthException(response.errorBody()?.string() ?: "Invalid Error"))
        }

    }

    suspend fun getOrdersByToken(
        accessToken: String
    ) : Result<List<Order>> {
        val response = remoteApiClient.getOrdersByToken(accessToken)

        return if (response.isSuccessful) {
            Result.success(response.body()?.orders ?: emptyList())
        }
        else {
            Result.failure(AuthException(response.errorBody()?.string() ?: "Invalid Error"))
        }
    }

    private suspend fun randomDelay() {
        delay((100L..1000L).random())
    }

    private fun <T> randomResult(data: T): Result<T> =
        if ((0..100).random() < 1) {
            Result.failure(RuntimeException())
        } else {
            Result.success(data)
        }
}






