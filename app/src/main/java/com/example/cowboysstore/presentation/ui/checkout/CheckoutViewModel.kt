package com.example.cowboysstore.presentation.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowboysstore.domain.entities.OrderRequestModel
import com.example.cowboysstore.domain.usecases.CreateOrderUseCase
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
class CheckoutViewModel @Inject constructor(
    private val createOrderUseCase: CreateOrderUseCase
) : ViewModel() {

    private val _orderCreatingResult: MutableStateFlow<OrderCreatingResult> = MutableStateFlow(OrderCreatingResult.Loading)
    val orderCreatingResult: StateFlow<OrderCreatingResult> = _orderCreatingResult

    fun createOrder(orderRequestModel: OrderRequestModel) {
        _orderCreatingResult.update {
            OrderCreatingResult.Loading
        }
        viewModelScope.launch {
            try {
                val orderNumber = withContext(Dispatchers.IO) {
                    createOrderUseCase.createOrder(orderRequestModel)
                }
                _orderCreatingResult.update {
                    OrderCreatingResult.Success(orderNumber)
                }
            } catch (e: LoadException) {
                _orderCreatingResult.update {
                    OrderCreatingResult.Error
                }
            }
        }
    }

    sealed class OrderCreatingResult {
        object Loading : OrderCreatingResult()

        class Success(val orderNumber: Int) : OrderCreatingResult()

        object Error : OrderCreatingResult()
    }
}