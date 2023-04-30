package com.example.cowboysstore.presentation.decorators

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cowboysstore.R

class SelectedItemDecorator(
    private val context: Context
    ) : RecyclerView.ItemDecoration() {

    private val selectedDrawable = ContextCompat.getDrawable(context, R.drawable.selected_item_border)

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val selectedView = parent.findViewHolderForAdapterPosition(selectedPosition)?.itemView
        selectedView?.let {
            selectedDrawable?.let { drawable ->
                val borderWidth = 8 // толщина границы
                val left = it.left - borderWidth
                val right = it.right + borderWidth
                val top = it.top - borderWidth / 2
                val bottom = it.bottom + borderWidth / 2
                drawable.setBounds(left, top, right, bottom)
                drawable.draw(canvas)
            }
        }
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
    }

    companion object {
        private var selectedPosition = RecyclerView.NO_POSITION
    }
}