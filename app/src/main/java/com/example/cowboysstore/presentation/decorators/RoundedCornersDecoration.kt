package com.example.cowboysstore.presentation.decorators

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RoundedCornersDecoration(
    private val radius: Float
    ) : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (position == 0) {
            outRect.top = radius.toInt()
        }

        if (position == itemCount - 1) {
            outRect.bottom = radius.toInt()
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)

        val left = parent.paddingLeft.toFloat()
        val right = parent.width - parent.paddingRight.toFloat()

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            val itemCount = state.itemCount

            if (position == 0) {
                val top = parent.paddingTop.toFloat()
                val bottom = view.bottom.toFloat()
                canvas.drawRoundRect(left, top, right, bottom, radius, radius, paint)
            }

            if (position == itemCount - 1) {
                val top = view.top.toFloat()
                val bottom = parent.height - parent.paddingBottom.toFloat()
                canvas.drawRoundRect(left, top, right, bottom, radius, radius, paint)
            }
        }
    }
}




