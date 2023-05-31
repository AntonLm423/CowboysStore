package com.example.cowboysstore.presentation.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.FragmentCatalogBinding
import com.example.cowboysstore.domain.entities.Product
import com.example.cowboysstore.presentation.adapters.ProductAdapter
import com.example.cowboysstore.presentation.customviews.ProgressContainer
import com.example.cowboysstore.presentation.decorators.DividerDecorator
import com.example.cowboysstore.presentation.ui.checkout.CheckoutFragment
import com.example.cowboysstore.presentation.ui.product.ProductFragment
import com.example.cowboysstore.presentation.ui.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogFragment : Fragment() {

    private lateinit var binding: FragmentCatalogBinding
    private val viewModel: CatalogViewModel by viewModels()

    private val productAdapter by lazy { ProductAdapter() }
    private val dividerDecorator by lazy { DividerDecorator(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Fetching list of products */
        viewModel.loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonProfile.setOnClickListener {
            navigateToProfile()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is CatalogViewModel.CatalogUiState.Loading -> {
                            binding.progressContainerCatalog.state = ProgressContainer.State.Loading
                        }
                        is CatalogViewModel.CatalogUiState.Success -> {
                            initializeRecyclerView(uiState.productsList)
                        }
                        is CatalogViewModel.CatalogUiState.Error -> {
                            initializeErrorState(uiState.errorResId, uiState.messageResId)
                        }
                    }

                }
            }
        }
    }

    /* Initializing recyclerViewProducts by products list */
    private fun initializeRecyclerView(productsList: List<Product>) {
        if (productsList.isEmpty()) {
            binding.progressContainerCatalog.state = ProgressContainer.State.Notice(
                R.string.catalog_notice_no_data,
                R.string.catalog_notice_no_data_message
            ) {
                /* Listener for buttonRefresh */
                binding.progressContainerCatalog.state = ProgressContainer.State.Loading
                viewModel.loadData()
            }
        } else {
            binding.progressContainerCatalog.state = ProgressContainer.State.Success
            binding.recyclerViewProducts.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = productAdapter
                addItemDecoration(dividerDecorator)
            }
            productAdapter.submitList(productsList)
            productAdapter.onClickListener = { productId ->
                navigateToProduct(productId)
            }
            productAdapter.onButtonBuyClickListener = { selectedProduct ->
                navigateToCheckout(selectedProduct)
            }
        }
    }

    /* Initializing progressContainer error state by error */
    private fun initializeErrorState(errorResId: Int, messageResId: Int) {
        binding.progressContainerCatalog.state = ProgressContainer.State.Notice(errorResId, messageResId) {
            /* Listener for buttonRefresh */
            binding.progressContainerCatalog.state = ProgressContainer.State.Loading
            viewModel.loadData()
        }
    }

    private fun navigateToCheckout(selectedProduct: Product) {
        parentFragmentManager.commit {
            replace(R.id.containerMain, CheckoutFragment.createInstance(selectedProduct))
            addToBackStack(null)
        }
    }

    private fun navigateToProfile() {
        parentFragmentManager.commit {
            replace(R.id.containerMain, ProfileFragment.createInstance(false))
            addToBackStack(null)
        }
    }

    private fun navigateToProduct(productId: String) {
        parentFragmentManager.commit {
            /* Sending productId to ProductFragment  */
            replace(R.id.containerMain, ProductFragment.createInstance(productId))
            addToBackStack(null)
        }
    }
}

