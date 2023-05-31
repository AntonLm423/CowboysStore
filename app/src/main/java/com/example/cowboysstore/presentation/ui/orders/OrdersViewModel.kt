package com.example.cowboysstore.presentation.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowboysstore.R
import com.example.cowboysstore.domain.entities.Order
import com.example.cowboysstore.domain.usecases.CancelOrderUseCase
import com.example.cowboysstore.domain.usecases.GetOrdersUseCase
import com.example.cowboysstore.domain.usecases.GetProductUseCase
import com.example.cowboysstore.utils.LoadException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase
) : ViewModel() {

    private val _allOrdersUiSate: MutableStateFlow<OrderUiState> = MutableStateFlow(OrderUiState.Loading)
    val allOrderUiState: StateFlow<OrderUiState> = _allOrdersUiSate

    private val _activeOrdersUiState: MutableStateFlow<OrderUiState> = MutableStateFlow(OrderUiState.Loading)
    val activeOrderUiState: StateFlow<OrderUiState> = _activeOrdersUiState

    private val _cancelingOrderUiState: MutableStateFlow<CancelingOrderUiState> = MutableStateFlow(CancelingOrderUiState.Error)
    val cancelingOrderUiState: StateFlow<CancelingOrderUiState> = _cancelingOrderUiState

    fun loadData() {
        _allOrdersUiSate.update {
            OrderUiState.Loading
        }
        _activeOrdersUiState.update {
            OrderUiState.Loading
        }

        viewModelScope.launch {
            try {
                val ordersList = withContext(Dispatchers.IO) {
                    getOrdersUseCase.getOrders()
                }

                // из-за особенности бека заменяю Product.Id именем продукта, чтобы не создавать новый класс и список
                val deferredTasks = ordersList.map {
                    async(Dispatchers.IO) {
                        val productTitle = getProductUseCase.getProductById(it.productId).title
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
                        e.errorResId ?: R.string.unknown_error, e.detailedErrorResId ?: R.string.unknown_error_message
                    )
                }
                _activeOrdersUiState.update {
                    OrderUiState.Error(
                        e.errorResId ?: R.string.unknown_error, e.detailedErrorResId ?: R.string.unknown_error_message
                    )
                }
            }
        }
    }

    fun cancelOrder(orderId: String) {
        _cancelingOrderUiState.update {
            CancelingOrderUiState.Loading
        }
        viewModelScope.launch {
            try {
                val order = withContext(Dispatchers.IO) {
                    cancelOrderUseCase.cancelOrder(orderId)
                }
                _cancelingOrderUiState.update {
                    CancelingOrderUiState.Success(order)
                }
            } catch (e: Exception) {
                _cancelingOrderUiState.update {
                    CancelingOrderUiState.Error
                }
            }
        }
    }

    fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    fun getOrderUiStateFlow(isActiveOrdersOnly: Boolean): StateFlow<OrderUiState> {
        return if (isActiveOrdersOnly) {
            activeOrderUiState
        } else {
            allOrderUiState
        }
    }

    fun updateLists(orderList: List<Order>) {
        _allOrdersUiSate.update {
            OrderUiState.Success(orderList)
        }
        _activeOrdersUiState.update {
            OrderUiState.Success(orderList.filter { it.status == "in_work" })
        }
    }

    sealed class OrderUiState {
        object Loading : OrderUiState()

        class Success(val ordersList: List<Order>) : OrderUiState()

        class Error(val errorResId: Int, val messageResId: Int) : OrderUiState()

        object Empty : OrderUiState()
    }

    sealed class CancelingOrderUiState {
        object Loading : CancelingOrderUiState()

        class Success(val order: Order) : CancelingOrderUiState()

        object Error : CancelingOrderUiState()
    }
}