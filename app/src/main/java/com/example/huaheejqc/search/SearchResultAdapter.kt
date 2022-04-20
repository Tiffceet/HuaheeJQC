package com.example.huaheejqc.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.huaheejqc.SearchDirections
import com.example.huaheejqc.data.SearchItem
import com.example.huaheejqc.databinding.FragmentSearchResultComBinding

class SearchResultAdapter (
    private val values: ArrayList<SearchItem>
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultAdapter.ViewHolder {
        return ViewHolder(
            FragmentSearchResultComBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bookTitle.text = item.title
        holder.bookDesc.text = item.description
        holder.container.setOnClickListener {
            val action = SearchDirections.actionSearchToBookDetails(item.id)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentSearchResultComBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val bookTitle = binding.bookTitle
        val bookDesc = binding.bookDesc
        val container = binding.searchResultComContainer
    }
}