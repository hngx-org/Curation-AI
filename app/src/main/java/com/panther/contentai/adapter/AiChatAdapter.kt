package com.panther.contentai.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.panther.contentai.R
import com.panther.contentai.databinding.ContentAiChatItemBinding
import com.panther.contentai.databinding.UserChatItemBinding
import com.panther.contentai.models.Chat

class AiChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val messages: MutableList<Chat> = mutableListOf()

    companion object{
        private const val VIEW_TYPE_SENDER = 1
        private const val VIEW_TYPE_RECEIVER = 2
    }

    private val differCallBack = object: DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.cookie == newItem.cookie
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }

    }

    inner class SendMessageViewHolder(val binding: UserChatItemBinding): RecyclerView.ViewHolder(binding.root)
    inner class AiChatMessageViewHolder(val binding: ContentAiChatItemBinding): RecyclerView.ViewHolder(binding.root)

    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {

            VIEW_TYPE_SENDER -> {
                val userMessageLayout = UserChatItemBinding.inflate(inflater, parent, false)
                SendMessageViewHolder(userMessageLayout)
            }
            else -> {
                val aiMessageLayout = ContentAiChatItemBinding.inflate(inflater, parent, false)
                AiChatMessageViewHolder(aiMessageLayout)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = differ.currentList[position]
        when (holder) {
            is SendMessageViewHolder -> {
                holder.binding.apply {
                    userMessageTv.text = chat.userMessage
                    userAiTv.text = "User"
                    profileImage
                }
            }
            is AiChatMessageViewHolder -> {
                holder.binding.apply {
                    contentAiReplyTv.text = chat.aiMessage
                    contentAiTv.text = R.string.content_ai.toString()
                    aiLogoImg
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val chat = differ.currentList[position]

        return when (chat.cookie) {

            chat.userMessage -> {
                VIEW_TYPE_SENDER
            }
            else -> {
                chat.aiMessage ; VIEW_TYPE_RECEIVER
            }
        }
    }


}