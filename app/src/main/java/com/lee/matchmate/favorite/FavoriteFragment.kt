package com.lee.matchmate.favorite

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.matchmate.common.AppGlobalContext
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentFavoriteBinding

class FavoriteFragment : ViewBindingBaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private val viewModel: FavoriteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentId = AppGlobalContext.prefs.getString("userId", "").toString()
        val favoriteAdapter = FavoriteAdapter(viewModel)

        binding.rvFav.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }

        viewModel.getFavoriteSpace(currentId){
            Log.e("favFragment",currentId)
            favoriteAdapter.submitList(it)
        }


    }

}