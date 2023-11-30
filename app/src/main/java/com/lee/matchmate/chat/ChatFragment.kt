package com.lee.matchmate.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.matchmate.R
import com.lee.matchmate.chat.detail.ChatDetailViewModel
import com.lee.matchmate.common.AppGlobalContext
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentChatBinding
import com.lee.matchmate.main.User

class ChatFragment : ViewBindingBaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {

    companion object {
        fun newInstance() = ChatFragment()
    }

    private val viewModel: ChatDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentId = AppGlobalContext.prefs.getString("userId", "").toString()
        val chatUserListAdapter = ChatUserListAdapter(viewModel)

        binding.rvChat.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatUserListAdapter
        }

        viewModel.getUserInfo(currentId) { user ->
            user?.let {
                chatUserListAdapter.submitList(it.chatId)
            }
        }

        viewModel.userInfo.observe(viewLifecycleOwner){
            if (it != null) {
                chatUserListAdapter.submitList(it.chatId.toList())
            }
        }


    }

}