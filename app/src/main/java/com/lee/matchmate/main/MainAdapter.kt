package com.lee.matchmate.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lee.matchmate.databinding.ItemMainSpaceBinding

class MainAdapter(private val itemList : ArrayList<Space>) : RecyclerView.Adapter<MainAdapter.ItemHolder>() {

    inner class ItemHolder(val binding: ItemMainSpaceBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ItemHolder {
        val binding = ItemMainSpaceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
/*        binding.root.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment()
            it.findNavController().navigate(action)
        }*/
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: MainAdapter.ItemHolder, position: Int) {
        var space : Space = itemList[position]
        holder.binding.tvItemName.text = space.name
        holder.binding.tvItemValue.text = space.value
        holder.binding.tvItemLocation.text = space.location
        holder.binding.root.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment()
            it.findNavController().navigate(action)
        }
    }
    override fun getItemCount() = itemList.size

    @SuppressLint("NotifyDataSetChanged")
    fun addData(space : Space){
        itemList.add(space)
        notifyDataSetChanged()
    }


}