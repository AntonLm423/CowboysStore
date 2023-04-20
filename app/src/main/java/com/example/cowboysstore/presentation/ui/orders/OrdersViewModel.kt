package com.example.cowboysstore.presentation.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowboysstore.R
import com.example.cowboysstore.data.model.Order
import com.example.cowboysstore.domain.usecases.GetOrdersUseCase
import com.example.cowboysstore.domain.usecases.GetProductUseCase
import com.example.cowboysstore.utils.LoadException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getProductUseCase : GetProductUseCase
) : ViewModel() {

    private val _allOrdersUiSate : MutableStateFlow<OrderUiState> = MutableStateFlow(OrderUiState.Loading)
    val allOrderUiState : StateFlow<OrderUiState> = _allOrdersUiSate

    private val _activeOrdersUiState : MutableStateFlow<OrderUiState> = MutableStateFlow(OrderUiState.Loading)
    val activeOrderUiState : StateFlow<OrderUiState> = _activeOrdersUiState

    fun loadData(accessToken : String) {

        _allOrdersUiSate.update {
            OrderUiState.Loading
        }

        _activeOrdersUiState.update {
            OrderUiState.Loading
        }

        viewModelScope.launch {
            try {
                val ordersList = withContext(Dispatchers.IO) {
                    getOrdersUseCase.getOrdersByToken(accessToken)
                }

                // из-за особенности бека заменяю Product.Id именем продукта, чтобы не создавать новый класс и список
                val deferredTasks = ordersList.map {
                    async(Dispatchers.IO) {
                        val productTitle = getProductUseCase.getProductById(accessToken, it.productId).title
                        it.productId = productTitle
                    }
                }

                deferredTasks.awaitAll()

                _allOrdersUiSate.update {
                    OrderUiState.Success(ordersList)
                }

                _activeOrdersUiState.update {
                    OrderUiState.Success(ordersList.filter { it.status == "in_work" })
                }
            } catch (e: LoadException) {
                _allOrdersUiSate.update {
                    OrderUiState.Error(
                        e.errorResId ?: R.string.unknown_error,
                        e.messageResId ?: R.string.unknown_error_message
                    )
                }
                _activeOrdersUiState.update {
                    OrderUiState.Error(
                        e.errorResId ?: R.string.unknown_error,
                        e.messageResId ?: R.string.unknown_error_message
                    )
                }
            }
        }
    }

    sealed class OrderUiState {
        object Loading : OrderUiState()
        class Success(val ordersList : List<Order>) : OrderUiState()
        class Error(
            val errorResId: Int,
            val messageResId : Int
        ) : OrderUiState()
    }
}