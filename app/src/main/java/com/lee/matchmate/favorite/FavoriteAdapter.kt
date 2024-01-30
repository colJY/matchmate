package com.lee.matchmate.favorite

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
import com.lee.matchmate.common.MatchmateAppContext
import com.lee.matchmate.common.Constants
import com.lee.matchmate.databinding.ItemMainSpaceBinding
import com.lee.matchmate.main.NewSpace

/**
 * FavoriteAdapter
 * 관심 목록을 리스트를 보여주기 위한 Adapter
 * @property viewModel
 */
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
        return ViewHolder( ItemMainSpaceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newSpace: NewSpace = getItem(position)
        val storageRef = FirebaseStorage.getInstance().reference
        val glideImage = newSpace.space.primaryImage.toUri().lastPathSegment?.let {
            storageRef.child(Constants.UPLOAD_IMAGES).child(it)
        }

        val currentId =
            MatchmateAppContext.prefs.getString(Constants.USER_ID, Constants.BLANK).toString()

        with(holder.binding){
            tvItemName.text = newSpace.space.title
            tvItemValue.text = newSpace.space.value
            tvItemLocation.text = newSpace.space.location

            btnItemFavorite.setIconResource(
                if (newSpace.space.fav.contains(currentId)) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )

            btnItemFavorite.setOnClickListener {
                viewModel.toggleFavState(currentId, newSpace) { isSuccess ->
                    if (isSuccess) {
                        holder.binding.btnItemFavorite.setIconResource(
                            if (newSpace.space.fav.contains(currentId)) R.drawable.baseline_favorite_24
                            else R.drawable.baseline_favorite_border_24
                        )
                    }
                }
            }

            if (newSpace.space.cond.isNotEmpty()) {
                cgItemCond.removeAllViews()
                newSpace.space.cond.trim().split(Constants.SPLIT).forEach {
                    cgItemCond.addView(Chip(holder.itemView.context).apply {
                        text = it
                    })
                }
            }

            root.setOnClickListener {
                val action =
                    FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(newSpace.id)
                it.findNavController().navigate(action)
            }
        }


        glideImage?.downloadUrl?.addOnSuccessListener {
            Glide.with(holder.itemView.context).load(it).into(holder.binding.ivItemSpace)
        }


    }
}


