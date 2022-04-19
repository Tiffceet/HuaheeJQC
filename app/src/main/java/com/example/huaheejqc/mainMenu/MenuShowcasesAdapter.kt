package com.example.huaheejqc.mainMenu

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.huaheejqc.MainMenuDirections
import com.example.huaheejqc.data.Book
import com.example.huaheejqc.databinding.FragmentMenuShowcaseBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class MenuShowcasesAdapter(
    private val values: ArrayList<Book>
) : RecyclerView.Adapter<MenuShowcasesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuShowcasesAdapter.ViewHolder {
        return ViewHolder(
            FragmentMenuShowcaseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        val storage = Firebase.storage
        var storageRef = storage.reference
        var imageRef = storageRef.child("images/${item.imageUrl}")
        val localFile = File.createTempFile("images", "jpg")

        holder.imageholder.setOnClickListener {
            it.findNavController().navigate(
                MainMenuDirections.actionMainMenuToBookDetails(
                    item.bookid
                )
            )
        }

        imageRef.getFile(localFile).addOnSuccessListener {
            val myBitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.imageholder.setImageBitmap(myBitmap)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentMenuShowcaseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imageholder = binding.imageView

        override fun toString(): String {
            return super.toString() + " '" + "'"
        }
    }
}