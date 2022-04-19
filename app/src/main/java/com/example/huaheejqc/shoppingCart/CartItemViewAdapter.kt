package com.example.huaheejqc.shoppingCart

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.huaheejqc.data.CartItem
import com.example.huaheejqc.databinding.FragmentCartItemBinding

class CartItemViewAdapter(
    private val values: ArrayList<CartItem>
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
        holder.prodPrice.text = item.price.toString()
    }

    override fun getItemCount(): Int = values.size

   inner class ViewHolder(binding: FragmentCartItemBinding) :
       RecyclerView.ViewHolder(binding.root) {
           val prodTitle: TextView = binding.productTitle
           val prodDesc: TextView = binding.productDesc
           val prodPrice: TextView = binding.productPrice

           override fun toString(): String {
               return super.toString() + " '" + prodTitle + "'"
           }
       }
}