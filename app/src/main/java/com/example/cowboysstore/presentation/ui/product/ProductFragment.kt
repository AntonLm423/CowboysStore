package com.example.cowboysstore.presentation.ui.product

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.cowboysstore.R
import com.example.cowboysstore.domain.entities.Product
import com.example.cowboysstore.databinding.FragmentProductBinding
import com.example.cowboysstore.presentation.adapters.ProductPreviewAdapter
import com.example.cowboysstore.presentation.adapters.ProductPreviewCarouselAdapter
import com.example.cowboysstore.presentation.adapters.ProductStructureAdapter
import com.example.cowboysstore.presentation.customviews.ProgressContainer
import com.example.cowboysstore.presentation.decorators.SelectedItemDecorator
import com.example.cowboysstore.presentation.decorators.SpacingItemDecorator
import com.example.cowboysstore.presentation.dialogs.SelectDialog
import com.example.cowboysstore.presentation.ui.checkout.CheckoutFragment
import com.example.cowboysstore.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private val viewModel: ProductViewModel by viewModels()

    private val productStructureAdapter by lazy { ProductStructureAdapter() }
    private val productPreviewCarouselAdapter by lazy { ProductPreviewCarouselAdapter() }
    private val productPreviewAdapter by lazy { ProductPreviewAdapter() }

    private val selectedItemDecorator by lazy { SelectedItemDecorator(requireContext()) }
    private val spacingItemDecorator by lazy { SpacingItemDecorator(false, resources.getDimensionPixelOffset(R.dimen.normal_50)) }

    private lateinit var selectDialog: SelectDialog

    private val productId by lazy {
        requireNotNull(arguments?.getString(Constants.PRODUCT_ID_KEY))
    }

    companion object {
        fun createInstance(productId: String): ProductFragment =
            ProductFragment().apply {
                arguments = bundleOf(
                    Constants.PRODUCT_ID_KEY to productId
                )
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadData(productId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarProduct.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is ProductViewModel.ProductUiState.Loading -> {
                            binding.progressContainerProduct.state = ProgressContainer.State.Loading
                        }
                        is ProductViewModel.ProductUiState.Success -> {
                            binding.progressContainerProduct.state = ProgressContainer.State.Success
                            refreshContent(uiState.product)
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

    private fun refreshContent(product: Product) =
        with(binding) {
            toolbarProduct.title = product.title
            textViewDescription.text = product.description
            textViewTitle.text = product.title
            textViewPrice.text = product.price.toString() + " ₽"
            textViewCategory.text = product.department

            if (product.badge.isNotEmpty()) {
                textViewBadge.text = product.badge.first().value
                textViewBadge.setBackgroundColor(Color.parseColor(product.badge.first().color))
                cardViewBadge.visibility = View.VISIBLE
            }

            /* Synchronize viewPagerPreview and RecyclerViewPreview */
            synchronizeCarousel(product.images)

            /* Sizes */
            autoCompleteTextViewSize.setText(product.sizes.firstOrNull { it.isAvailable }?.value)
            selectDialog = SelectDialog(
                requireContext(),
                product.sizes.filter { it.isAvailable }.map { it.value }
            )

            selectDialog.selectingItemAdapter.itemClickListener = { selectedItem ->
                autoCompleteTextViewSize.setText(selectedItem)
                selectDialog.hide()
            }

            autoCompleteTextViewSize.setOnClickListener {
                selectDialog.show()
            }

            /* Recycler view structure */
            recyclerViewStructure.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = productStructureAdapter
            }

            productStructureAdapter.submitList(product.details)

            buttonBuyNow.setOnClickListener {
                navigateToCheckout(product, autoCompleteTextViewSize.text.toString())
            }
        }

    /* Synchronize viewPagerPreview and RecyclerViewPreview */
    private fun synchronizeCarousel(images: List<String>) {
        with(binding) {
            viewPagerProductPreview.apply {
                adapter = productPreviewCarouselAdapter
            }

            viewPagerProductPreview.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    selectedItemDecorator.setSelectedPosition(position)
                    recyclerViewProductPreview.apply {
                        addItemDecoration(selectedItemDecorator)
                    }
                }
            })

            productPreviewCarouselAdapter.submitList(images)

            recyclerViewProductPreview.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = productPreviewAdapter
                addItemDecoration(spacingItemDecorator)
            }

            productPreviewAdapter.submitList(images)
            productPreviewAdapter.itemClickListener = { position ->
                viewPagerProductPreview.currentItem = position
            }
        }
    }

    private fun navigateToCheckout(product: Product, selectedSize: String) {
        parentFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.containerMain, CheckoutFragment.createInstance(product, selectedSize))
        }
    }
}