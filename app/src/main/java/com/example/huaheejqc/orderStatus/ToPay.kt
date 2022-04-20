package com.example.huaheejqc.orderStatus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huaheejqc.R
import com.example.huaheejqc.chatpackage.ChatRecyclerViewAdapter
import com.example.huaheejqc.data.Book
import com.example.huaheejqc.data.Order
import com.example.huaheejqc.databinding.FragmentPostedItemBinding
import com.example.huaheejqc.databinding.FragmentToPayBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.HashMap

class ToPay : Fragment() {
    private var _binding: FragmentToPayBinding? = null
    private val binding get() = _binding!!
    val userid = Firebase.auth.currentUser?.uid
    val stringID = userid.toString()
    private var dataArray:MutableList<Order> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentToPayBinding.inflate(inflater, container, false)
        val view = binding.root
        dataArray=ArrayList()
        val db = Firebase.firestore
//        val postedItemAdapter=OrderStatusRecycleViewAdapter(dataArray)

        db.collection("orders")
            .whereEqualTo("buyer", stringID)
            .whereEqualTo("status", "prepare_order")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("TAG", "${document.id} => ${document.data}")

                    val vval = document.data
                    vval as HashMap<String, Any>
//                    dataArray.add(Order(document.data["book"]as String,document.data["buyer"] as String, document.data["seller"]as String,document.data["ststus"]as String))
                }
//                postedItemAdapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
        binding.toPayRecycleView.layoutManager=LinearLayoutManager(context)
//        binding.toPayRecycleView.adapter=postedItemAdapter

        return view
    }

}