package com.example.cowboysstore.presentation.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowboysstore.R
import com.example.cowboysstore.data.model.Product
import com.example.cowboysstore.domain.usecases.GetProductUseCase
import com.example.cowboysstore.utils.LoadException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase
) : ViewModel() {

    private val _uiState : MutableStateFlow<ProductUiState> = MutableStateFlow(ProductUiState.Loading)
    val uiState : StateFlow<ProductUiState> = _uiState

    fun loadData(id : String) {
        _uiState.update {
            ProductUiState.Loading
        }

        viewModelScope.launch {
            try {
                val product = withContext(Dispatchers.IO) {
                    getProductUseCase.getProductById(id)
                }
                _uiState.update {
                    ProductUiState.Success(product)
                }
            } catch (e: LoadException) {
                _uiState.update {
                    ProductUiState.Error(
                        e.errorResId ?: R.string.unknown_error,
                        e.messageResId ?: R.string.unknown_error_message
                    )
                }
            }
        }
    }

    sealed class ProductUiState {
        object Loading : ProductUiState()
        class Success(val product: Product) : ProductUiState()
        class Error(val errorResId : Int, val messageResId : Int) : ProductUiState()
    }
}