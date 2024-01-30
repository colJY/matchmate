package com.lee.matchmate.chat.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.jakewharton.rxbinding4.view.clicks
import com.lee.matchmate.R
import com.lee.matchmate.chat.fcm.FCMViewModel
import com.lee.matchmate.chat.fcm.NotificationBody
import com.lee.matchmate.chat.fcm.NotificationData
import com.lee.matchmate.common.MatchmateAppContext
import com.lee.matchmate.common.Constants
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.databinding.FragmentChatDetailBinding
import com.lee.matchmate.main.decoration.MainDecoration
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * ChatDetailFragment - 사용자간의 채팅 Fragment
 *
 */
class ChatDetailFragment :
    ViewBindingBaseFragment<FragmentChatDetailBinding>(FragmentChatDetailBinding::inflate) {
    private val args: ChatDetailFragmentArgs by navArgs()

    companion object {
        fun newInstance() = ChatDetailFragment()
    }

    private val viewModel: ChatDetailViewModel by viewModels()
    private val fcmViewModel: FCMViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentId =
            MatchmateAppContext.prefs.getString(Constants.USER_ID, Constants.DEFAULT_USER_ID).toString()
        val roomId = args.documentID
        val otherUserId = roomId.replace(currentId, Constants.DEFAULT_USER_ID)
            .replace(Constants.REPLACE_USER_ID, Constants.DEFAULT_USER_ID)


        val adapter = ChatDetailAdapter(viewModel)

        with(binding.rvChatDetail) {
            apply {
                layoutManager = LinearLayoutManager(context)
                this.adapter = adapter
                addItemDecoration(
                    MainDecoration(
                        Constants.ITEM_DIVIDER,
                        R.color.lightGrey,
                        Constants.ITEM_DECORATION_SPACE
                    )
                )
            }
        }

        with(viewModel) {
            getChatData(args.documentID)
            chatMessages.observe(viewLifecycleOwner) { chatMessages ->
                adapter.submitList(chatMessages)
            }
            roomData.observe(viewLifecycleOwner) {
                binding.cgChatDetail.removeAllViews()
                it?.selectedCondList?.forEach { chipText ->
                    val chip = Chip(context).apply {
                        text = chipText
                    }
                    binding.cgChatDetail.addView(chip)
                }

            }
        }

        /**
         * RX 더블클릭 방지 이용
         * 채팅 전송 버튼을 클릭했을 때,
         */

        with(compositeDisposable) {
            binding.btnSendMessage
                .clicks()
                .observeOn(Schedulers.io())
                .throttleFirst(Constants.THROTTLE_FIRST_INTERVAL, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val messageText = binding.edChatDetail.text.toString()

                    if (messageText.isNotEmpty()) {
                        val chatMessage = ChatMessage(
                            sender = currentId,
                            message = messageText,
                            timestamp = System.currentTimeMillis()
                        )
                        viewModel.roomData.value?.let { roomData ->
                            val updatedChatMessageList = roomData.chatMessage + chatMessage
                            viewModel.insertChatToFireStore(
                                Chat(
                                    roomId,
                                    roomData.selectedCondList,
                                    updatedChatMessageList
                                ), roomId
                            )
                        }

                        binding.edChatDetail.text?.clear()

                        viewModel.getUserInfo(otherUserId) { user ->

                            if (user != null) {
                                val notificationBody = NotificationBody(
                                    to = user.fcmToken,
                                    data = NotificationData(
                                        title = Constants.NOTIFICATION_TITLE,
                                        message = messageText,
                                        roomId = roomId
                                    )
                                )
                                fcmViewModel.sendPushNotification(notificationBody)
                            }
                        }
                    }
                }
        }

        binding.tbChatDetail.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

}