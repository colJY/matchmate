package com.lee.matchmate.main.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
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
        val binding =
            ItemDetailImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailViewPagerAdapter.ViewHolder, position: Int) {
        val imageName: String = getItem(position)
        val storageRef = FirebaseStorage.getInstance().reference
        val glideImage = storageRef.child("uploadImages").child(imageName)

        glideImage.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(holder.itemView.context)
                .load(uri)
                .into(holder.binding.ivItemDetailImage)
        }.addOnFailureListener {

        }
    }
}

/*class DetailViewPagerAdapter() :
    ListAdapter<FireSpace, DetailViewPagerAdapter.ViewHolder>(DetailViewPagerAdapter.DiffCallback) {
    object DiffCallback : DiffUtil.ItemCallback<FireSpace>() {
        override fun areItemsTheSame(oldItem: FireSpace, newItem: FireSpace): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: FireSpace, newItem: FireSpace): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(val binding: ItemDetailImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailViewPagerAdapter.ViewHolder {
        val binding =
            ItemDetailImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailViewPagerAdapter.ViewHolder, position: Int) {

        val newFireSpace: FireSpace = getItem(position)
        Log.e("Check", "getItem: $newFireSpace")
        val storageRef = FirebaseStorage.getInstance().reference

        val imageProcess = newFireSpace.additionalImage.replace("[", "").replace("]", "")
        Log.e("asd", imageProcess)
        val images = imageProcess.split(",")

        images.forEachIndexed { index, it ->
            val glideImages = it.trim().toUri().lastPathSegment?.let { imageUri ->
                storageRef.child("uploadImages").child(imageUri)
            }
            Log.e("asd", it.trim())

            glideImages?.downloadUrl?.addOnSuccessListener { uri ->
                Glide.with(holder.itemView.context)
                    .load(uri)
                    .into(holder.binding.ivItemDetailImage) // ivItemDetailImage는 ViewHolder에 연결된 이미지뷰의 id입니다.
            }

        }
    }
}*/
/*
class DetailViewPagerAdapter(private val fireSpace: FireSpace) : RecyclerView.Adapter<DetailViewPagerAdapter.PageViewHolder>() {

    inner class PageViewHolder(val binding : ItemDetailImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding = ItemDetailImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageProcess = fireSpace.additionalImage.split(",").forEach {
            it.replace("[", "")?.replace("]", "").toString()
        }
        Log.e("asd",imageProcess.toString())
        val image : Uri = fireSpace.primaryImage.toUri()
        val glideImage = image.lastPathSegment?.let {
            storageRef.child("uploadImages").child(
                it
            )
        }
        glideImage?.downloadUrl?.addOnSuccessListener {
            Glide.with(holder.itemView.context).load(it).into(holder.binding.ivItemDetailImage)
        }
        holder.binding.ivItemDetailImage.setImageResource(image)
    }


}*/
