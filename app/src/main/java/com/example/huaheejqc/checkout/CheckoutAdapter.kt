package com.example.huaheejqc.checkout

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.huaheejqc.MainMenuDirections
import com.example.huaheejqc.data.Book
import com.example.huaheejqc.databinding.FragmentCheckoutItemBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class CheckoutAdapter(
    private val values: ArrayList<Book>
)  : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutAdapter.ViewHolder {
        return ViewHolder(
            FragmentCheckoutItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CheckoutAdapter.ViewHolder, position: Int) {
        val item = values[position]
        holder.orderTitle.text = item.title
        holder.orderDesc.text = item.description

        val storage = Firebase.storage
        var storageRef = storage.reference
        var imageRef = storageRef.child("images/${item.imageUrl}")
        val localFile = File.createTempFile("images", "jpg")

        holder.orderImage.setOnClickListener {
            it.findNavController().navigate(
                MainMenuDirections.actionMainMenuToBookDetails(
                    item.bookid
                )
            )
        }

        imageRef.getFile(localFile).addOnSuccessListener {
            val myBitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.orderImage.setImageBitmap(myBitmap)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentCheckoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val orderTitle = binding.orderTitle
        val orderDesc = binding.orderDesc
        val orderImage = binding.orderImage
    }
}