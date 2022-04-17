package com.example.huaheejqc.chatpackage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.huaheejqc.R
import com.example.huaheejqc.data.Chat
import com.example.huaheejqc.data.ChatMsg
import com.example.huaheejqc.databinding.ComponentMeMessageBinding
import com.example.huaheejqc.databinding.ComponentOtherMessageBinding
import com.example.huaheejqc.databinding.FragmentChatTargetBinding

class ConversationRecyclerViewAdapter(
    private val values: List<ChatMsg>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ME_MESSAGE = 1
    private val OTHER_MESSAGE = 2
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == ME_MESSAGE) {
            return SentMessageHolder(
                ComponentMeMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

//        if (viewType == OTHER_MESSAGE) {
        return ReceivedMessageHolder(
            ComponentOtherMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
//        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = values.get(position)
        if (item.user == "1") {
            return ME_MESSAGE
        } else {
            return OTHER_MESSAGE
        }
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = values[position]
        if (holder is SentMessageHolder) {
            holder.message.text = item.msg
            holder.timestamp.text = item.timestamp.toString()
        }
        if (holder is ReceivedMessageHolder) {
            holder.message.text = item.msg
            holder.timestamp.text = item.timestamp.toString()
        }

    }


    inner class SentMessageHolder(binding: ComponentMeMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val message = binding.textGchatMessageMe
        val timestamp = binding.textGchatTimestampMe
    }

    inner class ReceivedMessageHolder(binding: ComponentOtherMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val message = binding.textGchatMessageOther
        val timestamp = binding.textGchatTimestampOther
    }


}