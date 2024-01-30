package com.lee.matchmate.chat.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.lee.matchmate.common.MatchmateAppContext
import com.lee.matchmate.common.Constants
import com.lee.matchmate.databinding.ItemChatAnotherBinding
import com.lee.matchmate.databinding.ItemChatDetailBinding
import java.text.SimpleDateFormat
import java.util.Date

/**
 * ChatDetailAdapter - 채팅 메시지 표시를 위한 어댑터
 * 2명의 사용자를 구분하기 위해 두 종류의 ViewHolder를 사용
 * @property viewModel
 */
class ChatDetailAdapter(private val viewModel: ChatDetailViewModel) :
    ListAdapter<ChatMessage, RecyclerView.ViewHolder>(differ) {

    inner class MyMessageViewHolder(val binding: ItemChatDetailBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class OtherMessageViewHolder(val binding: ItemChatAnotherBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        val currentId = MatchmateAppContext.prefs.getString(Constants.USER_ID, Constants.BLANK).toString()
        return if (getItem(position).sender == currentId) {
            VIEW_TYPE_MY_MESSAGE
        } else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_MY_MESSAGE -> MyMessageViewHolder(
                ItemChatDetailBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )

            else -> OtherMessageViewHolder(ItemChatAnotherBinding.inflate(inflater, parent, false))
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatMessage = getItem(position)

        viewModel.getUserInfo(chatMessage.sender) { _ ->
            val user = viewModel.getCachedUserInfo(chatMessage.sender)

            val timestampLong = chatMessage.timestamp
            val formattedTimestamp = run {
                val date = Date(timestampLong)
                val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT)
                dateFormat.format(date)
            }

            when (holder) {
                is MyMessageViewHolder -> {

                    with(holder.binding){
                        tvItemChatDetailMessage.text = chatMessage.message
                        tvItemChatDetailTimestamp.text = formattedTimestamp
                    }
                }

                is OtherMessageViewHolder -> {
                    with(holder.binding){
                        tvItemChatAnotherDetailMessage.text = chatMessage.message
                        tvItemChatAnotherDetailTimestamp.text = formattedTimestamp
                    }

                    if (user != null) {
                        holder.binding.tvItemChatAnotherDetailNickname.text = user.userName
                        Glide.with(holder.itemView).load(user.profileImage)
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(Constants.ROUNDED_CORNER_RADIUS)))
                            .into(holder.binding.ivItemChatAnotherDetailProfile)
                    }
                }
            }
        }


    }

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 1
        private const val VIEW_TYPE_OTHER_MESSAGE = 2

        val differ = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem.timestamp == newItem.timestamp
            }

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem == newItem
            }
        }
    }
}

