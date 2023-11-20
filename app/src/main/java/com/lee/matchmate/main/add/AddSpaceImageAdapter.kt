package com.lee.matchmate.main.add

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lee.matchmate.databinding.ItemAddSpaceImageBinding

class AddSpaceImageAdapter : ListAdapter<Uri, AddSpaceImageAdapter.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Uri>() {
        override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(val binding: ItemAddSpaceImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAddSpaceImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uri = getItem(position)
        holder.binding.ivAddAdditionalImage.setImageURI(uri)
        holder.binding.ivAddAdditionalImage.background = null
    }
}

/*class AddSpaceImageAdapter : ListAdapter<ImageItems,RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<ImageItems>(){
        override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem === newItem

        }

        override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem == newItem
        }
    }
){
    override fun getItemCount(): Int {
        val originSize = currentList.size
        return if(originSize ==0) 0 else originSize.inc()
    }

    override fun getItemViewType(position: Int): Int {
        return if(itemCount.dec() == position) ITEM_LOAD_MORE else ITEM_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        when(viewType){
            ITEM_IMAGE -> {
                val binding = ItemAddSpaceImageBinding.inflate(inflater, parent, false)
                ImageViewHolder(binding)
            }
            else -> {

            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.
    }

    companion object {
        const val ITEM_IMAGE = 0
        const val ITEM_LOAD_MORE = 1
    }
}

sealed class ImageItems {
    data class Image(
        val uri : Uri,
    ) : ImageItems()
    object LoadMore : ImageItems()
}

class ImageViewHolder(private val binding: ItemAddSpaceImageBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item : ImageItems.Image){
        binding.ivAddAdditionalImage.setImageURI(item.uri)
    }
}*/





/*
class AddSpaceImageAdapter(private val itemList: List<Uri>) :
    RecyclerView.Adapter<AddSpaceImageAdapter.ItemHolder>() {

    inner class ItemHolder(val binding: ItemAddSpaceImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {

        val binding =
            ItemAddSpaceImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)

    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val image : Int = itemList[Uri]
        holder.binding.ivAddAdditionalImage.setImageResource(image)
    }

    override fun getItemCount() = itemList.size
}
*/

