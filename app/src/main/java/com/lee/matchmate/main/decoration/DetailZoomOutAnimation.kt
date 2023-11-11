package com.lee.matchmate.main.decoration

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.lee.matchmate.R
import kotlin.math.abs

private const val  MIN_SCALE = 0.85f
private const val MIN_ALPHA = 0.5f


class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {

        view.apply {
            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.viewpager_margin)
            val pagerWidth = resources.getDimensionPixelOffset(R.dimen.viewpager_margin)
            val screenWidth = resources.displayMetrics.widthPixels
            val offsetPx = screenWidth - pageMarginPx - pagerWidth

            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> {
                    alpha = 0f
                }

                position <= 1 -> {
                    val scaleFactor = Math.max(MIN_SCALE, 1 - abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    val translationXValue = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        horzMargin + vertMargin / 2
                    }

                    translationX = position * -offsetPx + translationXValue

                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    alpha = (MIN_ALPHA +
                            (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                }

                else -> {
                    alpha = 0f
                }
            }
        }
    }
}