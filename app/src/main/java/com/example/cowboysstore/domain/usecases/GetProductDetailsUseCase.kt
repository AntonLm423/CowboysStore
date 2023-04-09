package com.example.cowboysstore.domain.usecases

import com.example.cowboysstore.data.model.ProductDetails
import com.example.cowboysstore.data.repository.MockRepository
import com.example.cowboysstore.presentation.utils.LoadException
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(
    private val mockRepository: MockRepository
) {

    suspend fun getProductDetailsById(id : String) : ProductDetails {
        return mockRepository.getProductDetailsById(id).getOrElse {
            throw LoadException()
        }
    }

}