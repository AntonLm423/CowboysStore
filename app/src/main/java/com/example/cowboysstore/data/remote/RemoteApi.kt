package com.example.cowboysstore.data.remote

import com.example.cowboysstore.data.model.*
import com.example.cowboysstore.domain.entities.ChangedFields
import com.example.cowboysstore.domain.entities.OrderRequestModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

// Пока пагинации нет, получаю сразу все данные с заранее большим размером страницы
private const val PAGE_SIZE = 30

interface RemoteApi {
    /* Authorization by email and password */
    @Headers("Content-Type: application/json-patch+json")
    @PUT("user/signin")
    suspend fun signIn(
        @Body request: AuthRequest
    ): Response<AuthResponse>

    /* Fetch products by token */
    @GET("products")
    suspend fun getProducts(
        @Query("PageNumber") pageNumber: Int = 1,
        @Query("PageSize") pageSize: Int = PAGE_SIZE
    ): Response<ProductsResponse>

    /* Fetch user by token */
    @GET("user")
    suspend fun getProfile(): Response<ProfileResponse>

    /* Fetch product by id */
    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") productId: String,
    ): Response<ProductDetailsResponse>

    /* Fetch order by product */
    @GET("orders")
    suspend fun getOrders(
        @Query("PageNumber") pageNumber: Int = 1,
        @Query("PageSize") pageSize: Int = PAGE_SIZE
    ): Response<OrdersResponse>

    /* Cancel order by id */
    @PUT("orders/{id}")
    suspend fun cancelOrder(
        @Path("id") orderId: String
    ): Response<CancelOrderResponse>

    /* Change profile information */
    @PATCH("user")
    suspend fun changeProfile(
        @Body changedFields: List<ProfileChanges>
    ): Response<ChangedFields>

    @POST("orders/checkout")
    suspend fun createOrder(
        @Body orderRequestModel: OrderRequestModel
    ): Response<CreateOrderResponse>

    @GET("user/photo/{fileId}")
    suspend fun getUserPhoto(
        @Path("fileId") id: String
    ): Response<ResponseBody>
}