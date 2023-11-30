package com.lee.matchmate.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.matchmate.common.AppGlobalContext
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentFavoriteBinding
import com.lee.matchmate.main.MainViewModel

class FavoriteFragment : ViewBindingBaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentId = AppGlobalContext.prefs.getString("userId", "").toString()
        /*val favoriteAdapter = FavoriteAdapter(viewModel)

        binding.rvChat.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }*/
    }

}