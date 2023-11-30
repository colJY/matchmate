package com.lee.matchmate.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.matchmate.chat.detail.ChatDetailViewModel
import com.lee.matchmate.common.AppGlobalContext
import com.lee.matchmate.databinding.ItemChatUserListBinding
import com.lee.matchmate.databinding.ItemDetailImageBinding
import com.lee.matchmate.main.MainFragmentDirections
import com.lee.matchmate.main.User
class ChatUserListAdapter(private val viewModel: ChatDetailViewModel) : ListAdapter<String, ChatUserListAdapter.ViewHolder>(differ) {
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
        val currentId = AppGlobalContext.prefs.getString("userId", "").toString()
        val targetId = if (currentId == chatId.split("_")[0]) {
            chatId.split("_")[1]
        } else {
            chatId.split("_")[0]
        }

        viewModel.getUserInfo(targetId) { user ->
            holder.binding.tvItemNickname.text = user?.userName

            Glide.with(holder.binding.root)
                .load(user?.profileImage)
                .into(holder.binding.ivItemProfile)
        }

        holder.binding.root.setOnClickListener {
            val action = ChatFragmentDirections.actionChatFragmentToChatDetailFragment(chatId)
            it.findNavController().navigate(action)
        }
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}
/*


class ChatUserListAdapter(private val viewModel: ChatDetailViewModel) : ListAdapter<User, ChatUserListAdapter.ViewHolder>(differ) {
    inner class ViewHolder(val binding: ItemChatUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatUserListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatUser = getItem(position)
        val currentId = AppGlobalContext.prefs.getString("userId", "").toString()

        val targetId = if(currentId == chatUser.chatId)
        chatUser.chatId.forEach { it ->
            if(currentId == it.split("_")[0]){
                viewModel.getUserInfo(it.split("_")[0]){ user ->
                    holder.binding.tvItemNickname.text = user.userName
                }
            } else{
                viewModel.getUserInfo(it.split("_")[1]){ user ->
                    holder.binding.tvItemNickname.text = user.userName
                }
            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatId = getItem(position)
        val currentId = AppGlobalContext.prefs.getString("userId", "").toString()
        val targetId = if (currentId == chatId.split("_")[0]) {
            chatId.split("_")[1]
        } else {
            chatId.split("_")[0]
        }

        viewModel.getUserInfo(targetId) { user ->
            holder.binding.tvItemNickname.text = user?.userName
        }
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }

    }
}*/
