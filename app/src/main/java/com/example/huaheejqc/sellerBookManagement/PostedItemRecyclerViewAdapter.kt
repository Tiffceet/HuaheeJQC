package com.example.huaheejqc.sellerBookManagement

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.huaheejqc.R
import com.example.huaheejqc.SellerBookManagementDirections
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
        holder.price.text="RM " + item.price
        holder.viewDetails.setOnClickListener{
            Log.d("chin",item.bookid)
//            it.findNavController().navigate(R.id.action_sellerBookManagement_to_addBook)
            val action =
                SellerBookManagementDirections.actionSellerBookManagementToEditBookDetails(
                    item.bookid
                )
//                it.findNavController().navigate(R.id.action_chatFragment_to_conversationFragment)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: PosteditemListitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val title=binding.posteditemProducttitleTxt
            val price=binding.posteditemPriceTxt
            val viewDetails = binding.posteditemViewdetailsBtn
    }
}