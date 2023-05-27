package com.example.cowboysstore.presentation.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.example.cowboysstore.R
import kotlin.properties.Delegates

class ProgressPopupButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val imageButton: ImageButton
    private val progressBar: ProgressBar

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.view_progress_popup_button, this, true)
        imageButton = root.findViewById(R.id.imageButton)
        progressBar = root.findViewById(R.id.progressBar)
    }

    var isLoading: Boolean by Delegates.observable(false) { _, _, isLoading ->
        progressBar.isVisible = isLoading
        imageButton.isClickable = !isLoading
    }

    override fun setOnClickListener(l: OnClickListener?) {
        imageButton.setOnClickListener(l)
    }
}