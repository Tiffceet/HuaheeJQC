package com.example.huaheejqc.sellerBookManagement

import android.graphics.BitmapFactory
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.text.DecimalFormat

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

        item.imageUrl

        val storage = Firebase.storage
        var storageRef = storage.reference
        val timestamp = System.currentTimeMillis() / 1000L
        var imageRef = storageRef.child("images/${item.imageUrl}")
        val localFile = File.createTempFile("images", "jpg")

        imageRef.getFile(localFile).addOnSuccessListener {
            val myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath())
            holder.imageholder.setImageBitmap(myBitmap)
            Log.d("Succ", "succ")
        }.addOnFailureListener {
            Log.d("noob", "noob")
        }

        holder.price.text="RM " + DecimalFormat("####.00").format(item.price)
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
        val imageholder = binding.imageView4
    }
}