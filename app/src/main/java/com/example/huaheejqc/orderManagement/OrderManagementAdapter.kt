package com.example.huaheejqc.orderManagement

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.huaheejqc.SellerBookManagementDirections
import com.example.huaheejqc.data.Order
import com.example.huaheejqc.databinding.FragmentOrderManagementInterfaceBinding
import com.example.huaheejqc.databinding.FragmentPostedItemBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.text.DecimalFormat

class OrderManagementAdapter(
    private val values: ArrayList<Order>
) : RecyclerView.Adapter<OrderManagementAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderManagementAdapter.ViewHolder {
        return ViewHolder(
            FragmentOrderManagementInterfaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderManagementAdapter.ViewHolder, position: Int) {

//        val item = values[position]
//
//        holder.prodTitle.text = item.book
//
//        val storage = Firebase.storage
//        var storageRef = storage.reference
//        val timestamp = System.currentTimeMillis() / 1000L
//        var imageRef = storageRef.child("images/${item.imageUrl}")
//        val localFile = File.createTempFile("images", "jpg")
//
//        imageRef.getFile(localFile).addOnSuccessListener {
//            val myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath())
//            holder.imageholder.setImageBitmap(myBitmap)
//            Log.d("Succ", "succ")
//        }.addOnFailureListener {
//            Log.d("noob", "noob")
//        }
//
//        holder.price.text="RM " + DecimalFormat("####.00").format(item.price)
//        holder.viewDetails.setOnClickListener{
//            Log.d("chin",item.bookid)
////            it.findNavController().navigate(R.id.action_sellerBookManagement_to_addBook)
//            val action =
//                SellerBookManagementDirections.actionSellerBookManagementToEditBookDetails(
//                    item.bookid
//                )
////                it.findNavController().navigate(R.id.action_chatFragment_to_conversationFragment)
//            it.findNavController().navigate(action)
//        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentOrderManagementInterfaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val prodTitle: TextView = binding.productTitle
        val prodPrice: TextView = binding.productPrice

        override fun toString(): String {
            return super.toString() + " '" + prodTitle + "'"
        }
    }
}