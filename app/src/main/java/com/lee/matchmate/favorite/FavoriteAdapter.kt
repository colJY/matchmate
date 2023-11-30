package com.lee.matchmate.favorite

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.firebase.storage.FirebaseStorage
import com.lee.matchmate.R
import com.lee.matchmate.common.AppGlobalContext
import com.lee.matchmate.databinding.ItemMainSpaceBinding
import com.lee.matchmate.main.NewSpace

class FavoriteAdapter(
    private val viewModel: FavoriteViewModel
) :
    ListAdapter<NewSpace, FavoriteAdapter.ViewHolder>(DiffCallback) {
    object DiffCallback : DiffUtil.ItemCallback<NewSpace>() {
        override fun areItemsTheSame(oldItem: NewSpace, newItem: NewSpace): Boolean {
            return oldItem.id == newItem.id
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
        Log.e("favAdapter","asd")
        var newSpace: NewSpace = getItem(position)
        val storageRef = FirebaseStorage.getInstance().reference
        val glideImage = newSpace.space.primaryImage.toUri().lastPathSegment?.let {
            storageRef.child("uploadImages").child(
                it
            )
        }
        val currentId = AppGlobalContext.prefs.getString("userId", "").toString()
        if (newSpace != null) {
            holder.binding.tvItemName.text = newSpace.space.title
            holder.binding.tvItemValue.text = newSpace.space.value
            holder.binding.tvItemLocation.text = newSpace.space.location

            holder.binding.btnItemFavorite.setIconResource(
                if (newSpace.space.fav) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )

            if (newSpace.space.cond.isNotEmpty()) {
                holder.binding.cgItemCond.removeAllViews()
                newSpace.space.cond.trim().split(",").forEach {
                    holder.binding.cgItemCond.addView(Chip(holder.itemView.context).apply {
                        text = it
                    })
                }
            }

            holder.binding.btnItemFavorite.setOnClickListener {
                viewModel.toggleFavState(newSpace) { isSuccess ->
                    if (isSuccess) {
                        holder.binding.btnItemFavorite.setIconResource(
                            if (newSpace.space.fav) R.drawable.baseline_favorite_24
                            else R.drawable.baseline_favorite_border_24
                        )
                    }
                }
            }

            glideImage?.downloadUrl?.addOnSuccessListener {
                Glide.with(holder.itemView.context).load(it).into(holder.binding.ivItemSpace)

            }


            holder.binding.root.setOnClickListener {
                val action =
                    FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(newSpace.id)
                it.findNavController().navigate(action)
            }

        }


    }
}


