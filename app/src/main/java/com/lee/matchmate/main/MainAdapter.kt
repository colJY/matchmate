package com.lee.matchmate.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.firebase.storage.FirebaseStorage
import com.lee.matchmate.databinding.ItemMainSpaceBinding

class MainAdapter(private val itemList: List<NewSpace>?): ListAdapter<NewSpace, MainAdapter.ViewHolder>(DiffCallback){
    object DiffCallback  : DiffUtil.ItemCallback<NewSpace>(){
        override fun areItemsTheSame(oldItem: NewSpace, newItem: NewSpace): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: NewSpace, newItem: NewSpace): Boolean {
            return oldItem == newItem
        }
    }
    inner class ViewHolder(val binding : ItemMainSpaceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMainSpaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var newSpace: NewSpace = getItem(position)
        val storageRef = FirebaseStorage.getInstance().reference
        val glideImage = newSpace.space.primaryImage.toUri().lastPathSegment?.let {
            storageRef.child("uploadImages").child(
                it
            )
        }
        glideImage?.downloadUrl?.addOnSuccessListener {
            Glide.with(holder.itemView.context).load(it).into(holder.binding.ivItemSpace)
        }

        if (newSpace != null) {
            holder.binding.tvItemName.text = "이름"
            holder.binding.tvItemValue.text = newSpace.space.value
            holder.binding.tvItemLocation.text = newSpace.space.location

            newSpace.space.cond.trim().split(",").forEach {
                holder.binding.cgItemCond.addView(Chip(holder.itemView.context).apply {
                    text = it
                })
            }

            holder.binding.root.setOnClickListener {
                val action = MainFragmentDirections.actionMainFragmentToDetailFragment()
                it.findNavController().navigate(action)
            }
        }
    }
}

