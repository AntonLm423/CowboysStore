package com.example.cowboysstore.presentation.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cowboysstore.R
import com.example.cowboysstore.domain.entities.Order
import com.example.cowboysstore.databinding.FragmentOrdersListBinding
import com.example.cowboysstore.presentation.adapters.OrderAdapter
import com.example.cowboysstore.presentation.customviews.ProgressContainer
import com.example.cowboysstore.presentation.decorators.DividerDecorator
import com.example.cowboysstore.utils.Constants
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersListFragment : Fragment() {

    private lateinit var binding: FragmentOrdersListBinding
    private val viewModel: OrdersViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private val orderAdapter by lazy { OrderAdapter() }
    private val dividerDecorator by lazy { DividerDecorator(requireContext()) }

    private val isActiveOrdersOnly by lazy {
        requireNotNull(arguments?.getBoolean(Constants.ORDERS_LIST_KEY))
    }

    companion object {
        fun createInstance(isActiveOrdersOnly: Boolean): OrdersListFragment =
            OrdersListFragment().apply {
                arguments = bundleOf(Constants.ORDERS_LIST_KEY to isActiveOrdersOnly)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isActiveOrdersOnly) {
            collectOrders(true)
        } else {
            collectOrders(false)
        }
    }

    private fun initializeRecyclerViewOrders(ordersList: List<Order>) {
        binding.recyclerViewOrdersList.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            addItemDecoration(dividerDecorator)
        }
        orderAdapter.popupMenuCancelClickListener = { position ->
            cancelOrder(position, ordersList)
        }
        orderAdapter.submitList(ordersList)
    }

    private fun cancelOrder(position: Int, ordersList: List<Order>) {
        viewModel.cancelOrder(ordersList[position].id)
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cancelingOrderUiState.collect { uiState ->
                    when (uiState) {
                        is OrdersViewModel.CancelingOrderUiState.Loading -> {
                            updateOrderStatus(position, ordersList, OrderAdapter.STATUS_CANCELED)
                        }
                        is OrdersViewModel.CancelingOrderUiState.Error -> {
                            showSnackbar(getString(R.string.orders_cancel_error), true)
                            updateOrderStatus(position, ordersList, OrderAdapter.STATUS_IN_WORK)
                        }
                        is OrdersViewModel.CancelingOrderUiState.Success -> {
                            showSnackbar(
                                getString(
                                    R.string.orders_cancel_success,
                                    ordersList[position].number,
                                    ordersList[position].etd // TODO:!!!
                                ), false
                            )
                            updateOrderStatus(position, ordersList, OrderAdapter.STATUS_CANCELED)
                        }
                    }
                }
            }
        }
    }

    private fun updateOrderStatus(position: Int, ordersList: List<Order>, newStatus: String) {
        val updatedOrder = ordersList[position].copy(status = newStatus)
        val updatedList = ordersList.toMutableList()
        updatedList[position] = updatedOrder
        viewModel.updateLists(updatedList)
    }

    private fun collectOrders(isActiveOrdersOnly: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getOrderUiStateFlow(isActiveOrdersOnly).collect { uiState ->
                    when (uiState) {
                        is OrdersViewModel.OrderUiState.Loading -> {
                            binding.progressContainerOrdersList.state = ProgressContainer.State.Loading
                        }
                        is OrdersViewModel.OrderUiState.Success -> {
                            if (uiState.ordersList.isEmpty()) {
                                binding.progressContainerOrdersList.state = ProgressContainer.State.Notice(
                                    R.string.catalog_notice_error,
                                    R.string.catalog_notice_error_message // TODO: Перенести в юзкейс, и применять в ошибочном состоянии
                                )
                            } else {
                                binding.progressContainerOrdersList.state = ProgressContainer.State.Success
                                initializeRecyclerViewOrders(uiState.ordersList)
                            }
                        }
                        is OrdersViewModel.OrderUiState.Error -> {
                            binding.progressContainerOrdersList.state = ProgressContainer.State.Notice(
                                uiState.errorResId,
                                uiState.messageResId
                            ) {
                                viewModel.loadData()
                            }
                        }
                        is OrdersViewModel.OrderUiState.Empty -> {
                            // do nothing
                        }
                    }
                }
            }
        }
    }

    private fun showSnackbar(message: String, isError: Boolean) {
        val snackBar = Snackbar.make(binding.frameLayoutOrdersList, message, Snackbar.LENGTH_LONG)
        val backgroundColor = if (isError) R.color.red_input_failure else R.color.dark_blue
        snackBar.setBackgroundTint(ContextCompat.getColor(requireContext(), backgroundColor))
        snackBar.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        snackBar.show()
    }
}