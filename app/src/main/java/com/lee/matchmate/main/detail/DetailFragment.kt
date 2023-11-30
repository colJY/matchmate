package com.lee.matchmate.main.detail

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.chip.Chip
import com.lee.matchmate.chat.detail.Chat
import com.lee.matchmate.chat.detail.ChatDetailViewModel
import com.lee.matchmate.chat.detail.ChatMessage
import com.lee.matchmate.common.AppGlobalContext
import com.lee.matchmate.common.ViewBindingBaseFragment
import com.lee.matchmate.common.toastMessage
import com.lee.matchmate.databinding.FragmentDetailBinding
import com.lee.matchmate.main.User
import com.lee.matchmate.main.decoration.ZoomOutPageTransformer

class DetailFragment :
    ViewBindingBaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()
    private val chatDetailViewModel : ChatDetailViewModel by viewModels()
    private var selectedCondList: MutableSet<String> = mutableSetOf()

    companion object {
        fun newInstance() = DetailFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tbDetail.setOnMenuItemClickListener {
                val currentUserId = AppGlobalContext.prefs.getString("userId", "").toString()
                val otherUserId = viewModel.detailSpaceData.value?.userId

                if (otherUserId != null) {
                    val chatRoomId = listOf(currentUserId, otherUserId).sorted().joinToString(separator = "_")

                    chatDetailViewModel.getChatData(chatRoomId)  // 채팅방 데이터를 가져옵니다.

                    chatDetailViewModel.roomData.observe(viewLifecycleOwner) { roomData ->

                        if (roomData == null) {
                            // 채팅방이 존재하지 않으면 새 채팅방을 만듭니다.
                            val chat = Chat(chatRoomId, selectedCondList.toList(), listOf())
                            chatDetailViewModel.insertChatToFireStore(chat, chatRoomId)
                        } else {
                            // 채팅방이 이미 존재하면 기존 메시지를 유지합니다.
                            val updatedChatMessageList = roomData.chatMessage
                            val chat = Chat(chatRoomId, selectedCondList.toList(), updatedChatMessageList)
                            chatDetailViewModel.insertChatToFireStore(chat, chatRoomId)
                        }

                        viewModel.addChatIdToUser(chatRoomId, currentUserId)
                        viewModel.addChatIdToUser(chatRoomId, otherUserId)

                        val action = DetailFragmentDirections.actionDetailFragmentToChatDetailFragment(chatRoomId)
                        findNavController().navigate(action)
                    }
                } else {
                    toastMessage("알 수 없는 오류", activity as Activity)
                }

                return@setOnMenuItemClickListener true
            }

            tbDetail.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        viewModel.setDetail(args.documentID)

        viewModel.detailSpaceData.observe(viewLifecycleOwner) {
            with(binding) {
                val adapter = DetailViewPagerAdapter()
                vpDetail.adapter = adapter
                val imageNames = it?.additionalImage?.replace("[", "")?.replace("]", "")?.split(",")
                    ?.map { it.trim().toUri().lastPathSegment ?: "" }
                adapter.submitList(imageNames)
                vpDetail.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                vpDetail.setPageTransformer(ZoomOutPageTransformer())
                vpDetail.offscreenPageLimit = 1

                if (it != null) {
                    tvDetailLocationUser.text = it.location
                    tvDetailValueUser.text = it.value
                    cgDetailCond.removeAllViews()
                    it.cond.split(",").forEach { condText ->
                        val chip = Chip(context).apply {
                            text = condText
                            isCheckable = true
                        }

                        chip.setOnCheckedChangeListener { buttonView, isChecked ->
                            if (isChecked) {
                                selectedCondList.add(condText)
                                toastMessage("체크 되었습니다.", activity as Activity)
                            } else {
                                selectedCondList.remove(condText)
                                toastMessage("해제되었습니다.", activity as Activity)
                            }
                        }
                        cgDetailCond.addView(chip)
                    }
                }
            }
        }


    }




}