package com.example.cowboysstore.presentation.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowboysstore.data.repository.MockRepository
import com.example.cowboysstore.data.repository.Product
import com.example.cowboysstore.domain.usecases.GetProductsUseCase
import com.example.cowboysstore.utils.LoadException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatalogVievModel : ViewModel() {


    private val _uiState: MutableStateFlow<CatalogUiState> = MutableStateFlow(CatalogUiState.Loading)
    val uiState: StateFlow<CatalogUiState> = _uiState

    private val getProductsUseCase = GetProductsUseCase(MockRepository())

    init {
        loadData()
    }

    fun loadData() {
        _uiState.update {
            CatalogUiState.Loading
        }

        viewModelScope.launch() {
            try {
            val productList = withContext(Dispatchers.IO) {
                getProductsUseCase.getProducts()
            }
                _uiState.update {
                    CatalogUiState.Success(productList)
                }
            }
            catch (e: LoadException) {
                _uiState.update {
                    CatalogUiState.Error(e.errorResId, e.messageResId)
                }
            }
        }
    }

}

sealed class CatalogUiState {

    object Loading : CatalogUiState()
    class Success(val productsList : List<Product>) : CatalogUiState()
    class Error(val errorResId : Int, val messageResId : Int) : CatalogUiState()

}