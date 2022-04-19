package com.example.huaheejqc.sellerBookManagement

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huaheejqc.data.Book
import com.example.huaheejqc.databinding.FragmentCompletedOrderBinding
import com.example.huaheejqc.databinding.FragmentInTransitBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.HashMap

class InTransit : Fragment() {
    private var _binding: FragmentInTransitBinding? = null
    private val binding get() = _binding!!
    val userid = Firebase.auth.currentUser?.uid
    val stringID = userid.toString()
    private var dataArray:MutableList<Book> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInTransitBinding.inflate(inflater, container, false)
        val view = binding.root
        dataArray=ArrayList()
        val db = Firebase.firestore
        val postedItemAdapter=PostedItemRecyclerViewAdapter(dataArray)

        db.collection("books")
            .whereEqualTo("userid", stringID)
            .whereEqualTo("status", "InTransit")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("TAG", "${document.id} => ${document.data}")

                    val vval = document.data
                    vval as HashMap<String, Any>
                    dataArray.add(Book(document.data["title"]as String,document.data["author"] as String, document.data["price"]as Number,document.data["description"]as String,document.data["category"]as Number,document.data["page_amount"] as Number,document.data["status"]as String,document.data["userid"]as String,document.id))
                }
                postedItemAdapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
        binding.inTransitRecycleView.layoutManager= LinearLayoutManager(context)
        binding.inTransitRecycleView.adapter=postedItemAdapter
        return view
    }
}