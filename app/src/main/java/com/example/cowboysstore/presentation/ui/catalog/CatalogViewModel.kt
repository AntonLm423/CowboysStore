package com.example.cowboysstore.presentation.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowboysstore.R
import com.example.cowboysstore.data.model.Product
import com.example.cowboysstore.domain.usecases.GetProductsUseCase
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
class CatalogViewModel @Inject constructor(
    private val getProductsUseCase : GetProductsUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<CatalogUiState> = MutableStateFlow(CatalogUiState.Loading)
    val uiState: StateFlow<CatalogUiState> = _uiState

    fun loadData() {
        _uiState.update {
            CatalogUiState.Loading
        }

        viewModelScope.launch() {
            try {
            val productList = withContext(Dispatchers.IO) {
                getProductsUseCase.getProducts()
            }
                if(productList.isEmpty()) {
                    _uiState.update {
                        CatalogUiState.Error(
                            R.string.catalog_notice_no_data,
                            R.string.catalog_notice_no_data_message
                            )
                    }
                } else {
                    _uiState.update {
                        CatalogUiState.Success(productList)
                    }
                }
            }
            catch (e: LoadException) {
                _uiState.update {
                    CatalogUiState.Error(
                        e.errorResId ?: R.string.unknown_error,
                        e.messageResId ?: R.string.unknown_error_message
                    )
                }
            }
        }
    }

    sealed class CatalogUiState {

        object Loading : CatalogUiState()
        class Success(val productsList : List<Product>) : CatalogUiState()
        class Error(val errorResId : Int, val messageResId : Int) : CatalogUiState()

    }
}

