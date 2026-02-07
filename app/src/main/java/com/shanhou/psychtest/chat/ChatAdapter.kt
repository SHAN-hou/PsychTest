package com.shanhou.psychtest.chat

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.shanhou.psychtest.R
import com.shanhou.psychtest.databinding.ItemChatMessageBinding
import com.shanhou.psychtest.model.ChatMessage

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    private val messages = mutableListOf<ChatMessage>()

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun getMessages(): List<ChatMessage> = messages.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemChatMessageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size

    inner class MessageViewHolder(
        private val binding: ItemChatMessageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessage) {
            binding.tvMessage.text = message.content

            val layoutParams = binding.cardMessage.layoutParams as FrameLayout.LayoutParams

            if (message.isUser) {
                layoutParams.gravity = Gravity.END
                binding.cardMessage.setCardBackgroundColor(
                    binding.root.context.getColor(R.color.primary)
                )
                binding.tvMessage.setTextColor(
                    binding.root.context.getColor(R.color.white)
                )
            } else {
                layoutParams.gravity = Gravity.START
                binding.cardMessage.setCardBackgroundColor(
                    binding.root.context.getColor(R.color.white)
                )
                binding.tvMessage.setTextColor(
                    binding.root.context.getColor(R.color.on_surface)
                )
            }
            binding.cardMessage.layoutParams = layoutParams
        }
    }
}
