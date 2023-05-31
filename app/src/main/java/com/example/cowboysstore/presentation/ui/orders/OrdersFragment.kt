package com.example.cowboysstore.presentation.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.FragmentOrdersBinding
import com.example.cowboysstore.presentation.adapters.PagerTabAdapter
import com.example.cowboysstore.presentation.customviews.ProgressContainer
import com.example.cowboysstore.presentation.ui.catalog.CatalogFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private lateinit var binding : FragmentOrdersBinding
    private val viewModel : OrdersViewModel by viewModels()

    private lateinit var pagerTabAdapter: PagerTabAdapter

    private lateinit var tabNames : List<String>
    private val fragmentList = listOf(
        OrdersListFragment.createInstance(false),
        OrdersListFragment.createInstance(true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarOrders.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allOrderUiState.collect { uiState ->
                    when(uiState) {
                        is OrdersViewModel.OrderUiState.Loading -> {
                            binding.progressContainerOrders.state = ProgressContainer.State.Loading
                            initializeTabs()
                        }
                        is OrdersViewModel.OrderUiState.Success -> {
                            binding.progressContainerOrders.state = ProgressContainer.State.Success
                        }
                        is OrdersViewModel.OrderUiState.Error -> {
                            binding.progressContainerOrders.state = ProgressContainer.State.Notice(
                                uiState.errorResId,
                                uiState.messageResId,
                            ) {
                                viewModel.loadData()
                            }
                        }
                        is OrdersViewModel.OrderUiState.Empty -> {
                            binding.progressContainerOrders.setButtonText(R.string.orders_button_to_catalog_text)
                            binding.progressContainerOrders.state = ProgressContainer.State.Notice(
                                R.string.orders_no_data,
                                R.string.orders_no_data_message
                            ) {
                                navigateToCatalog()
                            }
                        }
                    }
                }
            }
        }


    }

    private fun initializeTabs() {
        pagerTabAdapter = PagerTabAdapter(this, fragmentList)

        binding.viewPagerTabs.apply {
            adapter = pagerTabAdapter
        }

        tabNames = listOf(
            getString(R.string.orders_tab_all),
            getString(R.string.orders_tab_active)
        )

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPagerTabs,
        ) { tab, pos ->
            tab.text = tabNames[pos]
        }.attach()
    }

    private fun navigateToCatalog() {
        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        parentFragmentManager.commit {
            replace(R.id.containerMain, CatalogFragment())
        }
    }
}