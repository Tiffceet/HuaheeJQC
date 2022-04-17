package com.example.huaheejqc.chatpackage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SortedList
import com.example.huaheejqc.R
import com.example.huaheejqc.data.Chat
import com.example.huaheejqc.data.ChatMsg
import com.example.huaheejqc.databinding.FragmentConversationBinding
import com.example.huaheejqc.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConversationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConversationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val args: ConversationFragmentArgs by navArgs()
    var conversationList: MutableList<ChatMsg> = ArrayList()
    var messagesIDs: HashMap<String, Boolean> = HashMap()
    private lateinit var logonUserID: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var _binding: FragmentConversationBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        logonUserID = auth.uid.toString()
        database = Firebase.database
        _binding = FragmentConversationBinding.inflate(inflater, container, false)
        val view = binding.root
        val chatId = args.chatId

        binding.recyclerGchat.layoutManager = LinearLayoutManager(context)
        binding.recyclerGchat.adapter = ConversationRecyclerViewAdapter(conversationList, logonUserID)
        binding.recyclerGchat.scrollToPosition(conversationList.size - 1)
        binding.buttonGchatSend.setOnClickListener(View.OnClickListener { view ->
            sendButtonOnClick()
        })

        val chatMessagesRef = database.getReference("chat-messages/$chatId")

        chatMessagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.value
                value as HashMap<String, HashMap<String, Any>>
                for ((msgId, content) in value) {
                    if (messagesIDs.containsKey(msgId)) {
                        continue
                    }
                    messagesIDs.set(msgId, true)
                    conversationList.add(
                        ChatMsg(
                            content["message"] as String,
                            (content["timestamp"] as Long).toInt(),
                            content["from"] as String
                        )
                    )
                    conversationList.sortBy { it.timestamp }
                    binding.recyclerGchat.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("firebase", error.message)
            }
        })

        return view
    }

    fun sendButtonOnClick() {
        conversationList.add(ChatMsg("uwu more data", 0, "1"))
        binding.recyclerGchat.adapter?.notifyItemInserted(conversationList.size - 1)
        binding.recyclerGchat.scrollToPosition(conversationList.size - 1)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConversationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConversationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}