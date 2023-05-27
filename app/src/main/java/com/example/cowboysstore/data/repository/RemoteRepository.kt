package com.example.cowboysstore.data.repository

import android.util.Log
import com.example.cowboysstore.R
import com.example.cowboysstore.data.model.*
import com.example.cowboysstore.data.remote.RemoteApi
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val remoteApiClient: RemoteApi
) {

    suspend fun getProducts(): Result<List<Product>> {

        try {
            val response = remoteApiClient.getProducts()

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

    suspend fun getProfile(): Result<Profile> {

        try {
            val response = remoteApiClient.getProfile()

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

    suspend fun getProductDetailsById(id: String): Result<Product> {

        try {
            val response = remoteApiClient.getProductById(id)

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

    suspend fun getOrders(): Result<List<Order>> {
        try {
            val response = remoteApiClient.getOrders()

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
            return Result.failure(
                LoadException(
                    R.string.unknown_error,
                    R.string.unknown_error_message
                )
            )
        }
    }

    suspend fun cancelOrder(orderId: String): Result<Order> {
        try {
            val response = remoteApiClient.cancelOrder(orderId)
            return if (response.isSuccessful) {
                val order = response.body()?.order ?: Order("", "", "", "", 1, "", "", 1, "", "")
                return Result.success(order)
            } else {
                Result.failure(LoadException())
            }
        } catch (e: Exception) {
            return Result.failure(
                LoadException(
                    R.string.unknown_error,
                    R.string.unknown_error_message
                )
            )
        }
    }

    suspend fun changeProfile(changedFields: List<ProfileChanges>): Result<Boolean> {
        return try {
            val response = remoteApiClient.changeProfile(changedFields)
            if (response.isSuccessful) {
                Log.d("TEXT_ABC", "Success")
                Result.success(true)
            } else {
                Log.d("TEXT_ABC", "Fail")
                Result.failure(Exception())
            }
        } catch (e: Exception) {
            Log.d("TEXT_ABC", "Excp")
            Result.failure(e)
        }
    }
}








