package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.R
import com.example.cowboysstore.data.model.Product
import com.example.cowboysstore.data.repository.RemoteRepository
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {


    suspend fun getProducts(accessToken : String) : List<Product> {

        return remoteRepository.getProducts(accessToken).getOrElse {
            throw LoadException(
                R.string.catalog_notice_error,
                R.string.catalog_notice_error_message)
        }
    }

}