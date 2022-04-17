package com.example.huaheejqc.chatpackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huaheejqc.R
import com.example.huaheejqc.data.Chat
import com.example.huaheejqc.data.ChatMsg
import com.example.huaheejqc.databinding.FragmentConversationBinding
import com.example.huaheejqc.databinding.FragmentLoginBinding

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
    var actual_data: MutableList<ChatMsg> = ArrayList()
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
        _binding = FragmentConversationBinding.inflate(inflater, container, false)
        val view = binding.root
        val chatId = args.chatId
        actual_data.add(
            ChatMsg(
                "This is a very ultra long message, how will androdi handle it hmm. When can i finish this assignmen aaaaaaaaaaaaaaaaaaaaaaaa",
                0,
                "1"
            )
        )
        actual_data.add(ChatMsg("hi", 0, "2"))
        binding.recyclerGchat.layoutManager = LinearLayoutManager(context)
        binding.recyclerGchat.adapter = ConversationRecyclerViewAdapter(actual_data)
        binding.recyclerGchat.scrollToPosition(actual_data.size-1)
        binding.buttonGchatSend.setOnClickListener(View.OnClickListener { view ->
            sendButtonOnClick()
        })

        return view
    }

    fun sendButtonOnClick() {
        actual_data.add(ChatMsg("uwu more data", 0, "1"))
        binding.recyclerGchat.adapter?.notifyItemInserted(actual_data.size - 1)
        binding.recyclerGchat.scrollToPosition(actual_data.size-1)
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