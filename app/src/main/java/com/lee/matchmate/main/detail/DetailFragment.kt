package com.lee.matchmate.main.detail

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.chip.Chip
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.common.toastMessage
import com.lee.matchmate.databinding.FragmentDetailBinding
import com.lee.matchmate.main.FireSpace
import com.lee.matchmate.main.decoration.ZoomOutPageTransformer

class DetailFragment :
    ViewBindingBaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()
    private var selectedCondList: MutableSet<String> = mutableSetOf()

    companion object {
        fun newInstance() = DetailFragment()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fireSpace: FireSpace = FireSpace()

        Log.e("args", args.toString())
        with(binding) {

/*            vpDetail.adapter = DetailViewPagerAdapter(fireSpace = )
            vpDetail.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            vpDetail.setPageTransformer(ZoomOutPageTransformer())
            vpDetail.offscreenPageLimit = 1*/

            tbDetail.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        viewModel.setDetail(args.documentID)

        viewModel.detailSpaceData.observe(viewLifecycleOwner) {
            with(binding) {
                val adapter = DetailViewPagerAdapter()
                vpDetail.adapter = adapter
                val imageNames = it?.additionalImage?.replace("[", "")?.replace("]", "")?.split(",")?.map { it.trim().toUri().lastPathSegment ?: "" }
                adapter.submitList(imageNames)
                vpDetail.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                vpDetail.setPageTransformer(ZoomOutPageTransformer())
                vpDetail.offscreenPageLimit = 1

                if (it != null) {
                    tvDetailLocationUser.text = it.location
                    tvDetailValueUser.text = it.value
                    cgDetailCond.removeAllViews()
                    it.cond.split(",").forEach{ condText ->
                        val chip = Chip(context).apply {
                            text = condText
                            isCheckable = true
                        }

                        chip.setOnCheckedChangeListener { buttonView, isChecked ->
                            if (isChecked) {
                                selectedCondList.add(condText)
                                toastMessage("체크 되었습니다.", activity as Activity)
                            } else {
                                selectedCondList.remove(condText)
                                toastMessage("해제되었습니다.", activity as Activity)
                            }
                        }
                        cgDetailCond.addView(chip)
                    }
                }
            }
        }


    }

    private fun getUserID(userId : String){

    }
}