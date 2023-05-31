package com.example.cowboysstore.presentation.ui.checkout

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.FragmentCheckoutBinding
import com.example.cowboysstore.domain.entities.OrderRequestModel
import com.example.cowboysstore.domain.entities.Product
import com.example.cowboysstore.presentation.ui.map.MapFragment
import com.example.cowboysstore.presentation.ui.orders.OrdersFragment
import com.example.cowboysstore.utils.Constants
import com.example.cowboysstore.utils.DateFormatter
import com.example.cowboysstore.utils.Validator
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class CheckoutFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutBinding
    private val viewModel: CheckoutViewModel by viewModels()

    private val product by lazy {
        requireNotNull(arguments?.getParcelable<Product>(Constants.PRODUCT_KEY))
    }

    private val selectedSize by lazy {
        /* If the size is not defined, the first available one is taken, if there are none available exception */
        arguments?.getString(Constants.SELECTED_SIZE_KEY) ?: product.sizes.first { size -> size.isAvailable }.value
    }

    /* Final order information for request */
    private var orderRequestModel = OrderRequestModel()

    /* Array of months for data picker */
    private val months by lazy {
        getString(R.string.checkout_delivery_months).split(" ").toTypedArray()
    }

    companion object {
        fun createInstance(product: Product, selectedSize: String? = null) = CheckoutFragment().apply {
            arguments = bundleOf(
                Constants.PRODUCT_KEY to product, Constants.SELECTED_SIZE_KEY to selectedSize
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFields()

        setFragmentResultListener(Constants.LOCATION_KEY) { _, bundle ->
            val result = bundle.getString(Constants.LOCATION_BUNDLE_KEY)
            if (result != null) {
                binding.autoCompleteTextViewHouse.setText(result)
                orderRequestModel.House = result
            }
        }
    }

    private fun initializeFields() = with(binding) {
        imageViewPreview.load(product.preview)
        textViewTitle.text = getString(R.string.checkout_tittle_text, selectedSize, product.title)
        textViewCategory.text = product.department
        textViewPrice.text = getString(R.string.price_template, product.price)
        buttonBuy.setText(getString(R.string.checkout_button_buy, product.price))

        numberPickerCount.numberChangeListener = { count ->
            buttonBuy.setText(getString(R.string.checkout_button_buy, product.price * count))
        }
        autoCompleteTextViewDeliveryDate.setOnClickListener {
            getDeliveryDate()
        }
        autoCompleteTextViewHouse.setOnClickListener {
            navigateToMap()
        }
        toolbarCheckout.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        buttonBuy.setOnClickListener {
            createOrder()
        }
    }

    private fun createOrder() {
        with(binding) {
            textInputLayoutHouse.error = null
            textInputLayoutApartment.error = null
            textInputLayoutDeliveryDate.error = null
            when {
                !Validator.isApartmentValid(editTextApartment.text.toString()) -> {
                    textInputLayoutApartment.error = getString(R.string.validation_error_message)
                }
                autoCompleteTextViewHouse.text.isEmpty() -> {
                    textInputLayoutHouse.error = getString(R.string.validation_error_message)
                }
                autoCompleteTextViewDeliveryDate.text.isEmpty() -> {
                    textInputLayoutDeliveryDate.error = getString(R.string.validation_error_message)
                }
                else -> {
                    orderRequestModel.Quantity = numberPickerCount.getCurrentNumber()
                    orderRequestModel.Size = selectedSize
                    orderRequestModel.Apartment = editTextApartment.text.toString()
                    orderRequestModel.ProductId = product.id

                    viewModel.createOrder(orderRequestModel)

                    viewLifecycleOwner.lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            viewModel.orderCreatingResult.collect { result ->
                                when (result) {
                                    is CheckoutViewModel.OrderCreatingResult.Loading -> {
                                        buttonBuy.isLoading = true
                                    }
                                    is CheckoutViewModel.OrderCreatingResult.Success -> {
                                        buttonBuy.isLoading = false
                                        navigateToOrders(result.orderNumber)
                                    }
                                    is CheckoutViewModel.OrderCreatingResult.Error -> {
                                        buttonBuy.isLoading = false
                                        showSnackBar(getString(R.string.checkout_error_message))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(binding.linearLayoutCheckout, message, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red_input_failure))
        snackBar.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        snackBar.show()
    }

    private fun getDeliveryDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), R.style.Theme_CowboysStore_DatePicker, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val dateText = "$selectedDayOfMonth ${months[selectedMonth]}"
                binding.autoCompleteTextViewDeliveryDate.setText(dateText)
                setDeliveryDate(selectedDayOfMonth, selectedMonth + 1, selectedYear)
            }, year, month, dayOfMonth
        )
        /* Min date to pick = current date + 24h */
        datePickerDialog.datePicker.minDate = calendar.timeInMillis + 86400000
        datePickerDialog.show()
    }

    private fun setDeliveryDate(day: Int, month: Int, year: Int) {
        // Выбор времени не предусмотрен макетом, беру случайное
        val currentDate = "$day/$month/$year/16:00/001"
        orderRequestModel.Etd = DateFormatter.formatDate(currentDate, "dd/M/yyyy/HH:mm/SSS", Constants.INPUT_DATE_FORMAT)
    }

    private fun navigateToOrders(orderNumber: Int) {
        parentFragmentManager.commit {
            replace(R.id.containerMain, OrdersFragment.createInstance(true, orderNumber))
        }
    }

    private fun navigateToMap() {
        parentFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.containerMain, MapFragment())
        }
    }
}