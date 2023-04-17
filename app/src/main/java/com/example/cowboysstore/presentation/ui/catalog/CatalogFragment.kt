package com.example.cowboysstore.presentation.ui.catalog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.FragmentCatalogBinding
import com.example.cowboysstore.presentation.adapters.ProductAdapter
import com.example.cowboysstore.presentation.customviews.ProgressContainer
import com.example.cowboysstore.presentation.decorators.DividerDecorator
import com.example.cowboysstore.presentation.ui.product.ProductFragment
import com.example.cowboysstore.presentation.ui.profile.ProfileFragment
import com.example.cowboysstore.utils.Constants
import com.example.cowboysstore.utils.getAccessToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogFragment : Fragment(), ProductAdapter.Listener {

    private val viewModel: CatalogViewModel by viewModels()

    private lateinit var binding: FragmentCatalogBinding

    private val productsAdapter by lazy { ProductAdapter(this) }
    private val dividerDecorator by lazy { DividerDecorator(requireContext()) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatalogBinding.inflate(inflater, container, false)
        viewModel.loadData(getAccessToken(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonProfile.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.containerMain, ProfileFragment())
                addToBackStack(null)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {

                        is CatalogViewModel.CatalogUiState.Loading -> {
                            binding.progressContainer.state = ProgressContainer.State.Loading
                        }

                        is CatalogViewModel.CatalogUiState.Success -> {
                            if (uiState.productsList.isEmpty()) {
                                binding.progressContainer.state = ProgressContainer.State.Notice(
                                    R.string.catalog_notice_no_data,
                                    R.string.catalog_notice_no_data_message
                                ) {/* button refresh listener */
                                    binding.progressContainer.state = ProgressContainer.State.Loading
                                    viewModel.loadData(getAccessToken(requireContext()))
                                }
                            } else {
                                binding.progressContainer.state = ProgressContainer.State.Success
                                binding.recyclerViewProducts.apply {
                                    layoutManager = LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                    adapter = productsAdapter
                                    addItemDecoration(dividerDecorator)
                                }
                                productsAdapter.submitList(uiState.productsList)
                            }
                        }

                        is CatalogViewModel.CatalogUiState.Error -> {
                            binding.progressContainer.state = ProgressContainer.State.Notice(
                                uiState.errorResId,
                                uiState.messageResId
                            ) {/* button refresh listener */
                                binding.progressContainer.state = ProgressContainer.State.Loading

                                viewModel.loadData(getAccessToken(requireContext()))
                            }
                        }
                    }

                }
            }
        }
    }

    override fun onClick(productId: String) {
        parentFragmentManager.commit {
            setFragmentResult(Constants.BUNDLE_KEY, bundleOf(Constants.PRODUCT_ID_KEY to productId))
            replace(R.id.containerMain, ProductFragment())
            addToBackStack(null)
        }
    }



}

