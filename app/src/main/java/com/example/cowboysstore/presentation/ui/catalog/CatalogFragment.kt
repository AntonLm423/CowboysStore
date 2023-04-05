package com.example.cowboysstore.presentation.ui.catalog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.FragmentCatalogBinding
import com.example.cowboysstore.presentation.adapters.ProductAdapter
import com.example.cowboysstore.presentation.customviews.ProgressContainer
import com.example.cowboysstore.presentation.decorators.DividerItemDecorator
import com.example.cowboysstore.presentation.decorators.RoundedCornersDecoration
import com.example.cowboysstore.presentation.ui.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogFragment : Fragment() {

    private val viewModel: CatalogViewModel by viewModels()

    private lateinit var binding: FragmentCatalogBinding

    private val productsAdapter by lazy { ProductAdapter() }
    private val itemDivider by lazy { DividerItemDecorator(requireContext()) }
    private val itemDecorator by lazy { RoundedCornersDecoration(8f) }


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
            parentFragmentManager.commit {
                replace(R.id.containerMain, ProfileFragment())
                addToBackStack(null)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {

                        is CatalogUiState.Loading -> {
                            binding.progressContainer.state = ProgressContainer.State.Loading
                        }

                        is CatalogUiState.Success -> {
                            if (uiState.productsList.isEmpty()) {
                                binding.progressContainer.state = ProgressContainer.State.Notice(
                                    R.string.catalog_notice_nodata,
                                    R.string.catalog_notice_nodata_message
                                ) {/* button refresh listener */
                                    binding.progressContainer.state = ProgressContainer.State.Loading
                                    viewModel.loadData()
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
                                    addItemDecoration(itemDivider)
                                    addItemDecoration(itemDecorator)
                                }
                                productsAdapter.submitList(uiState.productsList)
                            }
                        }

                        is CatalogUiState.Error -> {
                            binding.progressContainer.state = ProgressContainer.State.Notice(
                                uiState.errorResId,
                                uiState.messageResId
                            ) {/* button refresh listener */
                                binding.progressContainer.state = ProgressContainer.State.Loading
                                viewModel.loadData()
                            }
                        }
                    }

                }
            }
        }
    }
}

