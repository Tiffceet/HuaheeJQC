package com.example.huaheejqc.chatpackage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.huaheejqc.R
import com.example.huaheejqc.chatpackage.placeholder.PlaceholderContent.PlaceholderItem
import com.example.huaheejqc.data.Chat
import com.example.huaheejqc.databinding.FragmentChatTargetBinding


/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ChatRecyclerViewAdapter(
    private val values: List<Chat>
) : RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentChatTargetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.chatId.text = item.id
        holder.chatTitle.text = item.id
        holder.lastMessage.text = item.lastMsg
        holder.timestamp.text = item.timestamp.toString()

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentChatTargetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val chatId: TextView = binding.chatId
        val chatTitle: TextView = binding.chatTitle
        val lastMessage: TextView = binding.lastMessage
        val timestamp: TextView = binding.timestamp

        init {
            itemView.setOnClickListener {
                Log.d("Debug", "Item clicked !")
                val action =
                    ChatFragmentDirections.actionChatFragmentToConversationFragment(chatId.text.toString())
//                it.findNavController().navigate(R.id.action_chatFragment_to_conversationFragment)
                it.findNavController().navigate(action)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + chatTitle.text + "'"
        }
    }

}