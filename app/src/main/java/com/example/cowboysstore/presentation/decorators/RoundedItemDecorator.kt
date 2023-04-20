package com.example.cowboysstore.presentation.decorators

import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RoundedItemDecorator(private val radius: Float) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        val left = parent.paddingLeft.toFloat()
        val right = parent.width - parent.paddingRight.toFloat()

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.top + params.topMargin.toFloat()
            val bottom = child.bottom - params.bottomMargin.toFloat()

            val rect = RectF(left, top, right, bottom)
            canvas.drawRoundRect(rect, radius, radius, paint)
        }
    }

    companion object {
        private val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.TRANSPARENT
        }
    }
}