package com.example.cowboysstore.data.remote

import com.example.cowboysstore.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface RemoteApi
{

    /*    AUTHORIZATION BY EMAIL & PASSWORD */
    @Headers("accept: /", "Content-Type: application/json-patch+json")
    @PUT("user/signin")
    suspend fun authorization(
       @Body request : AuthRequest
        ) : Response<AuthResponse>

    /* GET PRODUCTS LIST */
    @GET("products")
    @Headers("accept: /")
    suspend fun getProducts(
        @Header("Authorization") accessToken : String,
        @Query("PageNumber") pageNumber : Int = 1,
        @Query("PageSize") pageSize : Int = 10
    ) : Response<ProductsResponse>


    @GET("user")
    @Headers("accept: /")
    suspend fun getProfileByToken(
        @Header("Authorization") accessToken : String
    ) : Response<ProfileResponse>

    @GET("products/{id}")
    @Headers("accept: /")
    suspend fun getProductById(
        @Header("Authorization") accessToken : String,
        @Path("id") productId : String,
        @Query("PageNumber") pageNumber : Int = 1,
        @Query("PageSize") pageSize : Int = 10
    ) : Response<ProductDetailsResponse>

    @GET("orders")
    @Headers("accept: /")
    suspend fun getOrdersByToken(
        @Header("Authorization") accessToken : String,
        @Query("PageNumber") pageNumber : Int = 1,
        @Query("PageSize") pageSize : Int = 10
    ) : Response<OrdersResponse>
}