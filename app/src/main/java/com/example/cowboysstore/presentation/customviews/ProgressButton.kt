package com.example.cowboysstore.presentation.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.example.cowboysstore.R
import kotlin.properties.Delegates

class ProgressButton @JvmOverloads constructor(
    context : Context,
    attrs : AttributeSet? = null,
    defStyleAttr : Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val button : Button
    private val progressBar : ProgressBar

    private var buttonText : String? = null

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.view_progress_button, this, true)
        button = root.findViewById(R.id.button)
        progressBar = root.findViewById(R.id.progressBar)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton)
        button.text = ta.getString(R.styleable.ProgressButton_text)
        ta.recycle()

    }

    var isLoading : Boolean by Delegates.observable(false) {_, _, isLoading ->
        progressBar.isVisible = isLoading

        button.isClickable = !isLoading
        button.textScaleX = if(isLoading) 0f else 1f
    }

    fun setText(resId : Int) {
        button.setText(resId)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        button.setOnClickListener(l)
    }

}