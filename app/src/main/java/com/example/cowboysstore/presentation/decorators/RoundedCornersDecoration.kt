package com.example.cowboysstore.presentation.decorators

import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RoundedCornersDecoration(
    private val radius: Float
    ) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val left = parent.paddingLeft.toFloat()
        val right = parent.width - parent.paddingRight.toFloat()

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)

            val rect = RectF(left, view.top.toFloat(), right, view.bottom.toFloat())
            c.drawRoundRect(rect, radius, radius, paint)
        }
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
    }
}




