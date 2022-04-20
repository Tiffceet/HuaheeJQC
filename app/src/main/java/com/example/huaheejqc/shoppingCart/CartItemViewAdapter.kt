package com.example.huaheejqc.shoppingCart

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.huaheejqc.data.CartItem
import com.example.huaheejqc.databinding.FragmentCartItemBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class CartItemViewAdapter(
    private val values: ArrayList<CartItem>,
    private val willDelete: ArrayList<String>
) : RecyclerView.Adapter<CartItemViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartItemViewAdapter.ViewHolder {
        return ViewHolder(
            FragmentCartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.prodTitle.text = item.title
        holder.prodDesc.text = item.description
        holder.prodPrice.text = String.format("RM %.2f", item.price)

        val storage = Firebase.storage
        var storageRef = storage.reference
        var imageRef = storageRef.child("images/${item.imageUrl}")
        val localFile = File.createTempFile("images", "jpg")

        imageRef.getFile(localFile).addOnSuccessListener {
            val myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath())
            holder.imageView.setImageBitmap(myBitmap)
        }

        holder.deleteBox.setOnClickListener{
            val thebox = it as CheckBox

            if (thebox.isChecked) {
                willDelete.add(item.id)
            } else {
                willDelete.remove(item.id)
            }
        }
    }

    override fun getItemCount(): Int = values.size

   inner class ViewHolder(binding: FragmentCartItemBinding) :
       RecyclerView.ViewHolder(binding.root) {
           val prodTitle: TextView = binding.productTitle
           val prodDesc: TextView = binding.productDesc
           val prodPrice: TextView = binding.productPrice
           val imageView = binding.imageView
           val deleteBox = binding.deleteBox

           override fun toString(): String {
               return super.toString() + " '" + prodTitle + "'"
           }
       }
}