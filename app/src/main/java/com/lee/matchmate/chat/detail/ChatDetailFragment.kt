package com.lee.matchmate.chat.detail

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.lee.matchmate.chat.fcm.FCMViewModel
import com.lee.matchmate.chat.fcm.NotificationBody
import com.lee.matchmate.chat.fcm.NotificationData
import com.lee.matchmate.common.AppGlobalContext
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.common.toastMessage
import com.lee.matchmate.databinding.FragmentChatDetailBinding

class ChatDetailFragment : ViewBindingBaseFragment<FragmentChatDetailBinding>(FragmentChatDetailBinding::inflate) {
    private val args: ChatDetailFragmentArgs by navArgs()
    companion object {
        fun newInstance() = ChatDetailFragment()
    }

    private val viewModel: ChatDetailViewModel by viewModels()
    private val fcmViewModel: FCMViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentId  = AppGlobalContext.prefs.getString("userId", "").toString()
        val roomId = args.documentID
        val otherUserId = roomId.replace(currentId, "").replace("_", "")



        val adapter = ChatDetailAdapter(viewModel)
        binding.rvChatDetail.layoutManager = LinearLayoutManager(context)
        binding.rvChatDetail.adapter = adapter

        viewModel.getChatData(args.documentID)

        viewModel.chatMessages.observe(viewLifecycleOwner){ chatMessages ->
            adapter.submitList(chatMessages)
        }

        viewModel.roomData.observe(viewLifecycleOwner){
            binding.cgChatDetail.removeAllViews()
            it?.selectedCondList?.forEach { chipText->
                val chip =Chip(context).apply {
                    text = chipText
                }
                binding.cgChatDetail.addView(chip)
            }

        }

        binding.btnSendMessage.setOnClickListener {
            val messageText = binding.edChatDetail.text.toString()

            if(messageText.isNotEmpty()){
                val chatMessage = ChatMessage(
                    sender = currentId,
                    message = messageText,
                    timestamp = System.currentTimeMillis()
                )
                viewModel.roomData.value?.let { roomData ->
                    val updatedChatMessageList = roomData.chatMessage + chatMessage
                    viewModel.insertChatToFireStore(Chat(roomId, roomData.selectedCondList, updatedChatMessageList), roomId)
                }
                binding.edChatDetail.text?.clear()

                viewModel.getUserInfo(otherUserId) { user ->

                    if (user != null) {
                        val notificationBody = NotificationBody(
                            to = user.fcmToken,
                            data = NotificationData(
                                title = "New message",
                                message = messageText
                            )
                        )
                        fcmViewModel.sendPushNotification(notificationBody)
                    }
                }
            }


        }

    }

}