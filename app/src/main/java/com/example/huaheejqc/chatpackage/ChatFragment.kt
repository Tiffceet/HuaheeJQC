package com.example.huaheejqc.chatpackage

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A fragment representing a list of Items.
 */
class ChatFragment : Fragment() {

    private var columnCount = 1
    private lateinit var database: FirebaseDatabase
    private var chatsArr: MutableList<Chat> = ArrayList()
    private val chatsIDs: HashMap<String, Boolean> = HashMap()
    private lateinit var logonUserID: String
    private lateinit var externalAdapter: ChatRecyclerViewAdapter
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
        val auth = Firebase.auth
        logonUserID = auth.uid.toString()
        database = Firebase.database
        val view = inflater.inflate(R.layout.fragment_chat_list, container, false)
//        for (i in 1..100) {
//            actual_data.add(Chat("ID" + i.toString(), "Last Message", 100))
//        }
//        actual_data.add(Chat("ID2", "Last Message", 100))
//        actual_data.add(Chat("ID3", "Last Message", 100))
//        actual_data.add(Chat("ID4", "Last Message", 100))

        val chatMembersRef = database.getReference("chat-members")
        chatMembersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                chatMemberOnChange(dataSnapshot.value)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.d("JQC", "Failed to read value")
            }
        })

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                externalAdapter = ChatRecyclerViewAdapter(chatsArr)
                adapter = externalAdapter
            }
        }
        return view
    }

    // Fires when new chat member added
    fun chatMemberOnChange(value: Any?) {
        Log.d("chatMemberOnChange", "I was fired")
        var adapter = externalAdapter
        value as HashMap<String, HashMap<String, Boolean>>

        // For Each chatID
        for ((chatId, chatMembers) in value) {
            // Skip if this chat dont belong to this user
            if(!chatMembers.containsKey("logonUserID")) {
                continue
            }
            if (chatsIDs.containsKey(chatId)) {
                // Do not check further if chat already exist
                continue
            }
            chatsIDs.set(chatId, true)

            // Get Chat Preview
            val chatsRef = database.getReference("chats/$chatId")
            chatsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val vval = dataSnapshot.value
                    if (vval == null) {
                        return
                    }
                    vval as HashMap<String, Any>
                    val inner_chatId = chatId
                    val lastMsg = vval["lastMsg"] as String
                    val timestamp = (vval["timestamp"] as Long).toInt()

                    var changedIndex =
                        chatsArr.indices.firstOrNull { i -> chatsArr[i].id == inner_chatId }
                    if (changedIndex == null) {
                        chatsArr.add(0, Chat(chatId, lastMsg, timestamp))
                    } else {
                        chatsArr[changedIndex].timestamp = timestamp
                        chatsArr[changedIndex].lastMsg = lastMsg
                    }
                    chatsArr.sortByDescending { it.timestamp }
                    adapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.d("JQC", "Failed to read value")
                }
            })
        }
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