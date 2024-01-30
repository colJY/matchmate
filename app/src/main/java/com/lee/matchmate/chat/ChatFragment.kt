package com.lee.matchmate.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.matchmate.R
import com.lee.matchmate.chat.detail.ChatDetailViewModel
import com.lee.matchmate.common.MatchmateAppContext
import com.lee.matchmate.common.Constants
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentChatBinding
import com.lee.matchmate.main.decoration.MainDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * ChatFragment - 채팅방 목록 확인을 위한 Fragment
 */
@AndroidEntryPoint
class ChatFragment : ViewBindingBaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {

    companion object {
        fun newInstance() = ChatFragment()
    }

    private val viewModel: ChatDetailViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentId =
            MatchmateAppContext.prefs.getString(Constants.USER_ID, Constants.BLANK).toString()
        val chatUserListAdapter = ChatUserListAdapter(viewModel)

        binding.rvChat.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatUserListAdapter
            addItemDecoration(
                MainDecoration(
                    Constants.ITEM_DIVIDER,
                    R.color.lightGrey,
                    Constants.ITEM_DECORATION_CHAT_SPACE
                )
            )
        }

        lifecycleScope.launch {

            chatViewModel.apply {
                chatUserInfo(userId = currentId)
                chatInfo.collect() { user ->
                    user?.let {
                        chatUserListAdapter.submitList(it.chatId)
                    }
                }
            }

        }
    }

}