package com.example.cowboysstore.presentation.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cowboysstore.data.model.Order
import com.example.cowboysstore.databinding.FragmentOrdersListBinding
import com.example.cowboysstore.presentation.adapters.OrderAdapter
import com.example.cowboysstore.presentation.customviews.ProgressContainer
import com.example.cowboysstore.presentation.decorators.DividerDecorator
import kotlinx.coroutines.launch

class ActiveOrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersListBinding
    private val viewModel : OrdersViewModel by activityViewModels()

    private val orderAdapter by lazy { OrderAdapter() }
    private val dividerDecorator by lazy { DividerDecorator(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersListBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.activeOrderUiState.collect { uiState ->
                    when (uiState) {
                        is OrdersViewModel.OrderUiState.Loading -> {
                            binding.progressContainerOrdersList.state = ProgressContainer.State.Loading
                        }
                        is OrdersViewModel.OrderUiState.Success -> {
                            binding.progressContainerOrdersList.state = ProgressContainer.State.Success
                            initializeRecyclerViewOrders(uiState.ordersList)
                        }
                        is OrdersViewModel.OrderUiState.Error -> {
                            binding.progressContainerOrdersList.state = ProgressContainer.State.Notice(
                               uiState.errorResId,
                               uiState.messageResId
                            ) {
                                viewModel.loadData()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initializeRecyclerViewOrders(ordersList : List<Order>) {
        binding.recyclerViewOrdersList.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            addItemDecoration(dividerDecorator)
        }
        orderAdapter.submitList(ordersList)
    }
}