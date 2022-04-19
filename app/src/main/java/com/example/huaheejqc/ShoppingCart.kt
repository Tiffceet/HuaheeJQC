package com.example.huaheejqc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huaheejqc.chatpackage.ChatRecyclerViewAdapter
import com.example.huaheejqc.data.CartItem
import com.example.huaheejqc.databinding.FragmentShoppingCartBinding
import com.example.huaheejqc.shoppingCart.CartItemViewAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShoppingCart.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShoppingCart : Fragment() {
    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!
    private var cartItemRefs:ArrayList<String> = ArrayList()
    private var cartItems:ArrayList<CartItem> = ArrayList()
    val userId = Firebase.auth.currentUser?.uid

    private lateinit var externalAdapter: CartItemViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cartItemRefs = ArrayList()
        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        val db = Firebase.firestore

        db.collection("carts")
            .document(userId.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // get cart items references
                    cartItemRefs.addAll(document.data?.get("books") as ArrayList<String>)

                    if (!cartItemRefs.isNullOrEmpty()) {
                        for (itemRef in cartItemRefs) {
                            db.collection("books")
                                .document(itemRef)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {
                                        val cartItem = CartItem(
                                            document.get("title") as String?,
                                            document.get("description") as String?,
                                            document.get("price") as Number?
                                        )
                                        cartItems.add(cartItem)

                                        externalAdapter.notifyDataSetChanged()
                                    } else {
                                        Log.d("ShoppingCart", "No such document")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d("ShoppingCart", "get failed with ", exception)
                                }
                        }
                    }
                } else {
                    Log.d("ShoppingCart", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ShoppingCart", "get failed with ", exception)
            }

        // Set the adapter
        externalAdapter = CartItemViewAdapter(cartItems)
        binding.cartRecyclerView.adapter = externalAdapter
        val externalLayoutManager = LinearLayoutManager(context)
        binding.cartRecyclerView.layoutManager = externalLayoutManager

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShoppingCart.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShoppingCart().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}