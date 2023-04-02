package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.R
import com.example.cowboysstore.data.model.Product
import com.example.cowboysstore.data.repository.MockRepository
import com.example.cowboysstore.utils.LoadException
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val mockRepository: MockRepository
) {


    suspend fun getProducts() : List<Product> {
        return mockRepository.getProducts().getOrElse {
            throw LoadException(R.string.catalog_notice_error, R.string.catalog_notice_error_message)
        }
    }

}