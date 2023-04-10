package com.example.cowboysstore.presentation.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.cowboysstore.data.model.ProductDetails
import com.example.cowboysstore.databinding.FragmentProductBinding
import com.example.cowboysstore.presentation.adapters.ProductStructureAdapter
import com.example.cowboysstore.presentation.adapters.PagerCarouselAdapter
import com.example.cowboysstore.presentation.adapters.RecyclerCarouselAdapter
import com.example.cowboysstore.presentation.decorators.SelectedItemDecorator
import com.example.cowboysstore.presentation.customviews.ProgressContainer
import com.example.cowboysstore.presentation.decorators.RoundedCornersDecoration
import com.example.cowboysstore.presentation.decorators.SpacingItemDecoration
import com.example.cowboysstore.presentation.dialogs.DialogSizeSelection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private lateinit var binding : FragmentProductBinding
    private val viewModel : ProductViewModel by viewModels()

    private val productStructureAdapter by lazy { ProductStructureAdapter() }
    private val pagerCarouselAdapter by lazy { PagerCarouselAdapter()  }
    private val recyclerCarouselAdapter by lazy { RecyclerCarouselAdapter() }

    private val roundedCornersDecoration by lazy { RoundedCornersDecoration(1f)}
    private val spacingItemDecorator by lazy { SpacingItemDecoration(4)}
    private val selectedItemDecorator by lazy { SelectedItemDecorator(requireContext()) }

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

            synchronizeCarousel(productDetails.images)

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

    private fun synchronizeCarousel(images : List<String>) {
        with(binding) {
        viewPagerProductPreview.apply {
            adapter = pagerCarouselAdapter
            // addItemDecoration(roundedCornersDecoration)
        }

        viewPagerProductPreview.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selectedItemDecorator.setSelectedPosition(position)
                recyclerViewProductPreview.apply {
                    addItemDecoration(selectedItemDecorator)
                }
            }
        })

        pagerCarouselAdapter.submitList(images)

        recyclerViewProductPreview.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_UP) {
                    val childView = rv.findChildViewUnder(e.x, e.y)
                    childView?.let {
                        val position = rv.getChildAdapterPosition(it)
                        viewPagerProductPreview.currentItem = position
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

        recyclerViewProductPreview.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = recyclerCarouselAdapter
            // addItemDecoration(roundedCornersDecoration)
            addItemDecoration(spacingItemDecorator)
        }

        recyclerCarouselAdapter.submitList(images)

    }
    }
}