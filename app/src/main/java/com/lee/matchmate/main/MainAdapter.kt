package com.lee.matchmate.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.google.firebase.storage.FirebaseStorage
import com.lee.matchmate.R
import com.lee.matchmate.common.MatchmateAppContext
import com.lee.matchmate.common.Constants
import com.lee.matchmate.databinding.ItemMainSpaceBinding
import kotlinx.coroutines.launch

class MainAdapter(
    private val viewModel: MainViewModel,
    private val lifecycleScope: LifecycleCoroutineScope
) :
    ListAdapter<NewSpace, MainAdapter.ViewHolder>(DiffCallback) {
    object DiffCallback : DiffUtil.ItemCallback<NewSpace>() {
        override fun areItemsTheSame(oldItem: NewSpace, newItem: NewSpace): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: NewSpace, newItem: NewSpace): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(val binding: ItemMainSpaceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMainSpaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentId =
            MatchmateAppContext.prefs.getString(Constants.USER_ID, Constants.BLANK).toString()
        val newSpace: NewSpace = getItem(position)
        val storageRef = FirebaseStorage.getInstance().reference
        val glideImage = newSpace.space.primaryImage.toUri().lastPathSegment?.let {
            storageRef.child(Constants.UPLOAD_IMAGES).child(
                it
            )
        }

        if (glideImage != null) {
            Glide.with(holder.itemView.context)
                .load(glideImage)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(80)))
                .placeholder(R.drawable.empty_image)
                .into(holder.binding.ivItemSpace)
        }

        lifecycleScope.launch {
            viewModel.isSuccess.collect {
                glideImage?.downloadUrl?.addOnSuccessListener {
                    Glide.with(holder.itemView.context)
                        .load(it)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(80)))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .placeholder(R.drawable.empty_image)
                        .into(holder.binding.ivItemSpace)
                }?.addOnFailureListener {
                }

            }
        }


        with(holder.binding) {
            tvItemName.text = newSpace.space.title
            tvItemValue.text = newSpace.space.value
            tvItemLocation.text = newSpace.space.location
            btnItemFavorite.setIconResource(
                if (newSpace.space.fav.contains(currentId)) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )
            if (newSpace.space.cond.isNotEmpty()) {
                cgItemCond.removeAllViews()
                newSpace.space.cond.trim().split(Constants.SPLIT).forEach {
                    cgItemCond.addView(Chip(holder.itemView.context).apply {
                        text = it
                    })
                }
            }

            btnItemFavorite.setOnClickListener {
                viewModel.toggleFavState(currentId, newSpace) { isSuccess ->
                    if (isSuccess) {
                        btnItemFavorite.setIconResource(
                            if (newSpace.space.fav.contains(currentId)) R.drawable.baseline_favorite_24
                            else R.drawable.baseline_favorite_border_24
                        )
                    }
                }
            }
            root.setOnClickListener {
                val action =
                    MainFragmentDirections.actionMainFragmentToDetailFragment(newSpace.id)
                it.findNavController().navigate(action)
            }
        }
    }
}

