package com.example.cowboysstore.presentation.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cowboysstore.data.model.ProductDetails
import com.example.cowboysstore.databinding.FragmentProductBinding
import com.example.cowboysstore.presentation.adapters.ProductStructureAdapter
import com.example.cowboysstore.presentation.adapters.ViewPagerAdapter
import com.example.cowboysstore.presentation.customviews.ProgressContainer
import com.example.cowboysstore.presentation.dialogs.DialogSizeSelection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private lateinit var binding : FragmentProductBinding
    private val viewModel : ProductViewModel by viewModels()

    private val productStructureAdapter by lazy { ProductStructureAdapter() }
    private val viewPagerAdapter by lazy { ViewPagerAdapter()  }

    private lateinit var dialogSizeSelection : DialogSizeSelection


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadData("12345")

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect{ uiState ->
                    when(uiState) {
                        is ProductViewModel.ProductUiState.Loading -> {
                            binding.progressContainerProduct.state = ProgressContainer.State.Loading
                        }
                        is ProductViewModel.ProductUiState.Success -> {
                            binding.progressContainerProduct.state = ProgressContainer.State.Success

                            refreshContent(uiState.productDetails)

                        }
                        is ProductViewModel.ProductUiState.Error -> {
                            binding.progressContainerProduct.state = ProgressContainer.State.Notice(
                                uiState.errorResId,
                                uiState.messageResId
                            )
                        }
                    }
                }
            }
        }
    }


    private fun refreshContent(
        productDetails: ProductDetails
    ) {

        with(binding) {
            textViewDescription.text = productDetails.description
            textViewTitle.text = productDetails.title
            textViewPrice.text = productDetails.price
            textViewCategory.text = productDetails.category

            textViewBadge.visibility = if (productDetails.hit) View.VISIBLE else View.GONE

            viewPagerProductPreview.apply {
                adapter = viewPagerAdapter
            }

            viewPagerAdapter.submitList(productDetails.images)

            dialogSizeSelection = DialogSizeSelection(
                requireContext(),
                productDetails.availableSizes)

            textInputLayoutSize.setOnClickListener {
                dialogSizeSelection.show()
            }

            recyclerViewStructure.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = productStructureAdapter
            }
        }
        productStructureAdapter.submitList(productDetails.structure)
    }


}