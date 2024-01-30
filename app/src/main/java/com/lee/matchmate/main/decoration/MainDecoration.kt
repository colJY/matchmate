package com.lee.matchmate.main.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * MainDecoration
 * RecyclerView의 경계선의 속성 설정을 위한 MainDecoration
 * @property dividerHeight
 * @property dividerColor
 * @property space
 */
class MainDecoration(
    private val dividerHeight: Int,
    private val dividerColor: Int = Color.TRANSPARENT,
    private val space: Int
) : RecyclerView.ItemDecoration() {


    private val paint = Paint()
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        myDivider(c, parent, dividerColor)
        super.onDraw(c, parent, state)
    }


    private fun myDivider(c: Canvas, parent: RecyclerView, color: Int) {
        paint.color = color

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val param = child.layoutParams as RecyclerView.LayoutParams

            val dividerTop = child.bottom + param.bottomMargin
            val dividerBottom = dividerTop + dividerHeight

            c.drawRect(
                //Pyo
                child.left.toFloat(),
                child.right.toFloat(),
                dividerTop.toFloat(),
                dividerBottom.toFloat(),
                paint
            )
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        //Pyo
        outRect.bottom = dividerHeight
        outRect.top = space
        outRect.bottom = space
    }
}