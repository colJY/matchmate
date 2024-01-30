package com.lee.matchmate.main.decoration

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.lee.matchmate.R
import com.lee.matchmate.common.Constants
import kotlin.math.abs
import kotlin.math.max

/**
 * Zoom out page transformer
 * ViewPager2의 Image 스와이프 중 Zoom 되는 애니메이션
 */
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
                    val scaleFactor = max(Constants.MIN_SCALE, 1 - abs(position))
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

                    alpha = (Constants.MIN_ALPHA +
                            (((scaleFactor - Constants.MIN_SCALE) / (1 - Constants.MIN_SCALE)) * (1 - Constants.MIN_ALPHA)))
                }

                else -> {
                    alpha = 0f
                }
            }
        }
    }
}