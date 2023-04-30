package com.example.cowboysstore.presentation.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecorator(
    private val isVertical: Boolean,
    private val spacing: Int
    ) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (isVertical) {
            outRect.bottom = spacing
        } else {
            outRect.right = if (position == itemCount - 1) 0 else spacing
        }
    }
}