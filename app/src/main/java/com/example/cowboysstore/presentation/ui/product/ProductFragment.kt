package com.example.cowboysstore.presentation.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.cowboysstore.data.model.Product
import com.example.cowboysstore.databinding.FragmentProductBinding
import com.example.cowboysstore.presentation.adapters.PagerCarouselAdapter
import com.example.cowboysstore.presentation.adapters.ProductStructureAdapter
import com.example.cowboysstore.presentation.adapters.RecyclerCarouselAdapter
import com.example.cowboysstore.presentation.customviews.ProgressContainer
import com.example.cowboysstore.presentation.decorators.SelectedItemDecorator
import com.example.cowboysstore.presentation.dialogs.DialogSizeSelection
import com.example.cowboysstore.utils.Constants
import com.example.cowboysstore.utils.getAccessToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private lateinit var binding : FragmentProductBinding
    private val viewModel : ProductViewModel by viewModels()

    private val productStructureAdapter by lazy { ProductStructureAdapter() }
    private val pagerCarouselAdapter by lazy { PagerCarouselAdapter()  }
    private val recyclerCarouselAdapter by lazy { RecyclerCarouselAdapter() }

    private val selectedItemDecorator by lazy { SelectedItemDecorator(requireContext()) }

    private lateinit var dialogSizeSelection : DialogSizeSelection

    private lateinit var productId : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater,container, false)

        setFragmentResultListener(Constants.BUNDLE_KEY) { _, bundle ->
            productId = bundle.getString(Constants.PRODUCT_ID_KEY)!!
            viewModel.loadData(getAccessToken(requireContext()), productId)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect{ uiState ->
                    when(uiState) {
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

    private fun refreshContent(
        product: Product
    ) {
        with(binding) {

            /* Default textview */
            textViewDescription.text = product.description
            textViewTitle.text = product.title
            textViewPrice.text = product.price.toString() + " ₽"
            textViewCategory.text = product.department
            textViewBadge.visibility = if (true) View.VISIBLE else View.GONE

            /* Preview carousel */
            synchronizeCarousel(product.images)

            /* Sizes */
            autoCompleteTextViewSize.setText(product.sizes.firstOrNull { it.isAvailable }?.value)
            dialogSizeSelection = DialogSizeSelection(
                requireContext(),
                product.sizes.filter { it.isAvailable }.map { it.value }
            )

            textInputLayoutSize.setOnClickListener {
                dialogSizeSelection.show()
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
        }
        productStructureAdapter.submitList(product.details)
    }

    private fun synchronizeCarousel(images : List<String>) {
        with(binding) {
        viewPagerProductPreview.apply {
            adapter = pagerCarouselAdapter
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
        }

        recyclerCarouselAdapter.submitList(images)

    }
    }
}