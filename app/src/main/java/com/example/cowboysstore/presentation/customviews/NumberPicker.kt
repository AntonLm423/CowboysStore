package com.example.cowboysstore.presentation.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.cowboysstore.R

class NumberPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_MAX_NUMBER = 5
    }

    var numberChangeListener: (Int) -> Unit = { }

    private var maxNumber: Int = DEFAULT_MAX_NUMBER
        set(value) {
            require(value > 0) { "maxNumber must be greater than 0" }
            field = value
            updateButtonsClickableStatus()
        }

    private var currentNumber: Int = 1
        set(value) {
            field = value
            textViewNumber.text = value.toString()
            updateButtonsClickableStatus()
        }

    private val buttonMinus: ImageView
    private val textViewNumber: TextView
    private val buttonPlus: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_number_picker, this, true)

        buttonMinus = findViewById(R.id.imageViewMinus)
        textViewNumber = findViewById(R.id.textViewNumber)
        textViewNumber.text = currentNumber.toString()
        buttonPlus = findViewById(R.id.imageViewPlus)

        buttonPlus.setOnClickListener { incrementNumber() }
        buttonMinus.setOnClickListener { decrementNumber() }
    }

    fun setMaxValue(maxNumber: Int) {
        require(maxNumber > 0) { "maxNumber must be greater than 0" }
        this.maxNumber = maxNumber
    }

    fun getCurrentNumber(): Int = currentNumber

    private fun incrementNumber() {
        if (currentNumber < maxNumber) {
            currentNumber++
            numberChangeListener(currentNumber)
        } else {
            disableButton(buttonPlus)
        }
    }

    private fun decrementNumber() {
        if (currentNumber > 1) {
            currentNumber--
            numberChangeListener(currentNumber)
        } else {
            disableButton(buttonMinus)
        }
    }

    private fun updateButtonsClickableStatus() {
        when (currentNumber) {
            1 -> disableButton(buttonMinus)
            maxNumber -> disableButton(buttonPlus)
            else -> {
                enableButton(buttonMinus)
                enableButton(buttonPlus)
            }
        }
    }

    private fun disableButton(button: ImageView) {
        button.isClickable = false
        button.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
    }

    private fun enableButton(button: ImageView) {
        button.isClickable = true
        button.setBackgroundResource(R.color.selector_clickable_item_grey)
    }
}