package com.example.huaheejqc.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.huaheejqc.Search
import com.example.huaheejqc.data.SearchVal
import com.example.huaheejqc.databinding.FragmentSearchTagBinding

class SearchAdapter(
    private val searchClass: Search,
    private val values: ArrayList<SearchVal>
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        return ViewHolder(
            FragmentSearchTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.tagText.text = item.display
        holder.tagText.setOnClickListener {
            if (item.category != null) {
                if (item.value != null) {
                    searchClass.selectedTags[item.category!!] = item
                } else {
                    searchClass.selectedTags[item.category!!] = null
                }

                searchClass.clickSearch()
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentSearchTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val tagText = binding.tagButton

            override fun toString(): String {
                return super.toString() + " '" + tagText + "'"
            }
        }
}