package com.lee.matchmate.main.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.lee.matchmate.R
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentDetailBinding
import com.lee.matchmate.main.decoration.ZoomOutPageTransformer

class DetailFragment :
    ViewBindingBaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private val viewModel: DetailViewModel by viewModels()

    companion object {
        fun newInstance() = DetailFragment()
    }

    private val spaceDetailImages = arrayListOf<Int>(
        R.drawable.image_room,
        R.drawable.kakao_login_medium_wide
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding){
            vpDetail.adapter = DetailViewPagerAdapter(spaceDetailImages)
            vpDetail.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            vpDetail.setPageTransformer(ZoomOutPageTransformer())
            vpDetail.offscreenPageLimit = 1

            tbDetail.setNavigationOnClickListener{
                findNavController().popBackStack()
            }
        }

    }

}