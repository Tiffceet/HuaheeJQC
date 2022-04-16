package com.example.huaheejqc.chatpackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.huaheejqc.R
import com.example.huaheejqc.chatpackage.placeholder.PlaceholderContent
import com.example.huaheejqc.data.Chat

/**
 * A fragment representing a list of Items.
 */
class ChatFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_list, container, false)
        val actual_data: MutableList<Chat> = ArrayList()
        for (i in 1..100) {
            actual_data.add(Chat("ID" + i.toString(), "Last Message", 100))
        }
        actual_data.add(Chat("ID2", "Last Message", 100))
        actual_data.add(Chat("ID3", "Last Message", 100))
        actual_data.add(Chat("ID4", "Last Message", 100))
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = ChatRecyclerViewAdapter(actual_data)
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}