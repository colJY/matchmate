package com.lee.matchmate.main.detail

import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lee.matchmate.R
import com.lee.matchmate.databinding.ItemDetailImageBinding

class DetailViewPagerAdapter(private val imageList : ArrayList<Int>) : RecyclerView.Adapter<DetailViewPagerAdapter.PageViewHolder>() {

    inner class PageViewHolder(val binding : ItemDetailImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding = ItemDetailImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val image : Int = imageList[position]
        holder.binding.ivItemDetailImage.setImageResource(image)
    }

    override fun getItemCount() = imageList.size


}