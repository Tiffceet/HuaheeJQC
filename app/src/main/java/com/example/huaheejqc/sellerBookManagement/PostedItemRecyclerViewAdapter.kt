package com.example.huaheejqc.sellerBookManagement

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.huaheejqc.R
import com.example.huaheejqc.chatpackage.ChatFragmentDirections
import com.example.huaheejqc.chatpackage.ChatRecyclerViewAdapter
import com.example.huaheejqc.data.Book
import com.example.huaheejqc.data.Chat
import com.example.huaheejqc.databinding.FragmentChatTargetBinding
import com.example.huaheejqc.databinding.PosteditemListitemBinding

class PostedItemRecyclerViewAdapter(private val values: List<Book>) :
    RecyclerView.Adapter<PostedItemRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostedItemRecyclerViewAdapter.ViewHolder {
        return ViewHolder(
            PosteditemListitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PostedItemRecyclerViewAdapter.ViewHolder, position: Int) {
        val item = values[position]
        holder.title.text=item.title
        holder.status.text=item.status
        holder.viewDetails.setOnClickListener{
            Log.d("chin",item.title)
            it.findNavController().navigate(R.id.action_sellerBookManagement_to_addBook)

        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: PosteditemListitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val title=binding.posteditemProducttitleTxt
            val status=binding.posteditemStatusTxt
            val viewDetails = binding.posteditemViewdetailsBtn
    }
}