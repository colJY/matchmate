package com.lee.matchmate.main.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.lee.matchmate.common.Constants
import com.lee.matchmate.databinding.ItemDetailImageBinding

class DetailViewPagerAdapter() :
    ListAdapter<String, DetailViewPagerAdapter.ViewHolder>(DiffCallback) {
    object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(val binding: ItemDetailImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailViewPagerAdapter.ViewHolder {
        return ViewHolder(ItemDetailImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DetailViewPagerAdapter.ViewHolder, position: Int) {
        val imageName: String = getItem(position)
        val storageRef = FirebaseStorage.getInstance().reference
        val glideImage = storageRef.child(Constants.IMAGE_COLLECTION_NAME).child(imageName)

        glideImage.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(holder.itemView.context)
                .load(uri)
                .into(holder.binding.ivItemDetailImage)
        }
    }
}

