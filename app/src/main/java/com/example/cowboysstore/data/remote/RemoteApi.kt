package com.example.cowboysstore.data.remote

import com.example.cowboysstore.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface RemoteApi
{
    /* Authorization by email and password */
    @Headers("Content-Type: application/json-patch+json")
    @PUT("user/signin")
    suspend fun authorization(
       @Body request : AuthRequest
        ) : Response<AuthResponse>

    /* Fetch products by token */
    @GET("products")
    suspend fun getProducts(
        @Query("PageNumber") pageNumber : Int = 1,
        @Query("PageSize") pageSize : Int = 10
    ) : Response<ProductsResponse>

    /* Fetch user by token */
    @GET("user")
    suspend fun getProfile() : Response<ProfileResponse>

    /* Fetch product by id */
    @GET("products/{id}")
    @Headers("accept: /")
    suspend fun getProductById(
        @Path("id") productId : String,
        @Query("PageNumber") pageNumber : Int = 1,
        @Query("PageSize") pageSize : Int = 10
    ) : Response<ProductDetailsResponse>

    /* Fetch order by product */
    @GET("orders")
    @Headers("accept: /")
    suspend fun getOrders(
        @Query("PageNumber") pageNumber : Int = 1,
        @Query("PageSize") pageSize : Int = 10
    ) : Response<OrdersResponse>
}