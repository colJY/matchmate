package com.lee.matchmate.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.lee.matchmate.chat.detail.ChatDetailViewModel
import com.lee.matchmate.common.AppGlobalContext
import com.lee.matchmate.common.Constants
import com.lee.matchmate.databinding.ItemChatUserListBinding

class ChatUserListAdapter(
    private val viewModel: ChatDetailViewModel
) : ListAdapter<String, ChatUserListAdapter.ViewHolder>(DiffCallback) {
    object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(val binding: ItemChatUserListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatUserListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatId = getItem(position)
        val currentId =
            AppGlobalContext.prefs.getString(Constants.USER_ID, Constants.BLANK).toString()
        val targetId = if (currentId == chatId.split(Constants.UNDERSCORE)[0]) {
            chatId.split(Constants.UNDERSCORE)[1]
        } else {
            chatId.split(Constants.UNDERSCORE)[0]
        }

        viewModel.getUserInfo(targetId) { user ->
            holder.binding.tvItemNickname.text = user?.userName

            Glide.with(holder.binding.root)
                .load(user?.profileImage)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(Constants.ROUNDING_RADIUS)))
                .into(holder.binding.ivItemProfile)
        }

        holder.binding.root.setOnClickListener {
            val action = ChatFragmentDirections.actionChatFragmentToChatDetailFragment(chatId)
            it.findNavController().navigate(action)
        }
    }
}

