package com.example.cowboysstore.data.repository

import com.example.cowboysstore.R
import com.example.cowboysstore.data.model.AuthRequest
import com.example.cowboysstore.data.model.Order
import com.example.cowboysstore.data.model.Product
import com.example.cowboysstore.data.model.Profile
import com.example.cowboysstore.data.remote.RemoteApi
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val remoteApiClient : RemoteApi
) {

    suspend fun getProducts(accessToken: String): Result<List<Product>> {

        try {
            val response = remoteApiClient.getProducts(accessToken)

            return if (response.isSuccessful) {
                Result.success(response.body()?.productsList ?: emptyList())
            } else {
                return Result.failure(LoadException(R.string.unknown_error, R.string.unknown_error_message))
            }
        } catch (e: Exception) {
            throw LoadException(
                R.string.catalog_notice_error,
                R.string.catalog_notice_error_message
            )
        }

    }

    suspend fun getProfileByToken(accessToken: String): Result<Profile> {

        try {
            val response = remoteApiClient.getProfileByToken(accessToken)

            return if (response.isSuccessful) {
                Result.success(response.body()?.responseData?.profile ?: Profile())
            } else {
                return Result.failure(
                    LoadException(
                        R.string.unknown_error,
                        R.string.unknown_error_message
                    )
                )
            }
        } catch (e: Exception) {
            throw LoadException(
                R.string.profile_data_loading_error,
                R.string.profile_data_loading_message
            )
        }
    }

    suspend fun authorization(email: String, password: String): Result<String> {

        try {
            val response = remoteApiClient.authorization(AuthRequest(email, password))

            return if (response.isSuccessful) {
                Result.success(response.body()?.responseBody?.accessToken ?: "")
            } else {
                when (response.code()) {
                    400 -> {
                        return Result.failure(LoadException(R.string.sign_in_auth_user_not_registered))
                    }
                    422 -> {
                        return Result.failure(LoadException(R.string.sign_in_auth_invalid))
                    }
                    else -> {
                        return Result.failure(LoadException(R.string.unknown_error))
                    }
                }
            }
        } catch (e: Exception) {
            throw LoadException(
                R.string.sign_in_auth_error
            )
        }
    }

    suspend fun getProductDetailsById(
        accessToken: String,
        id: String
    ): Result<Product> {

        try {
            val response = remoteApiClient.getProductById(accessToken, id)

            return if (response.isSuccessful) {
                val product = response.body()?.product ?: Product()

                /* If wrong request product id */
                return if (product.id.isEmpty()) {
                    Result.failure(LoadException())
                } else {
                    Result.success(product)
                }

            } else {
                return Result.failure(
                    LoadException(
                        R.string.unknown_error,
                        R.string.unknown_error_message
                    )
                )
            }
        } catch (e: Exception) {
            throw LoadException(
                R.string.unknown_error,
                R.string.unknown_error_message
            )
        }
    }

    suspend fun getOrdersByToken(
        accessToken: String
    ): Result<List<Order>> {
        try {
            val response = remoteApiClient.getOrdersByToken(accessToken)

            return if (response.isSuccessful) {
                val orders = response.body()?.orders ?: emptyList()
                /* If no orders exists */
                if (orders.isEmpty()) {
                    return Result.failure(
                        LoadException(
                            R.string.orders_no_data,
                            R.string.orders_no_data_message
                        )
                    )
                } else {
                    return Result.success(orders)
                }
            } else {
               return Result.failure(LoadException())
            }
        } catch (e: Exception) {
            return Result.failure(LoadException(
                R.string.unknown_error,
                R.string.unknown_error_message
            ))
        }
    }
}








