package com.example.huaheejqc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
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
    private var totalPrice = 0.00;
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

        var isEdit: Boolean = false;
        var willDelete: ArrayList<String> = ArrayList()

            db.collection("carts")
            .document(userId.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document?.data != null) {
                    // get cart items references
                    cartItemRefs.addAll(document.data?.get("books") as ArrayList<String>)

                    getBookData()
                } else {
                    Log.d("ShoppingCart", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ShoppingCart", "get failed with ", exception)
            }

        // Set the adapter
        externalAdapter = CartItemViewAdapter(cartItems, willDelete)
        binding.cartRecyclerView.adapter = externalAdapter
        val externalLayoutManager = LinearLayoutManager(context)
        binding.cartRecyclerView.layoutManager = externalLayoutManager



        binding.editButton.setOnClickListener {
            isEdit = true
            it.visibility = View.GONE
            binding.finishButton.visibility = View.VISIBLE
            binding.placeOrder.visibility = View.GONE
            binding.removeItem.visibility = View.VISIBLE
        }

        binding.finishButton.setOnClickListener {
            isEdit = false
            it.visibility = View.GONE
            binding.editButton.visibility = View.VISIBLE
            binding.removeItem.visibility = View.GONE
            binding.placeOrder.visibility = View.VISIBLE

        }

        binding.removeItem.setOnClickListener{
            if (isEdit) {
                for (ref in willDelete) {
                    cartItemRefs.remove(ref)
                }
                val updatedData: HashMap<String, ArrayList<String>> = HashMap()
                updatedData.put("books", cartItemRefs)
                db.collection("carts")
                    .document(userId.toString())
                    .set(updatedData)
                getBookData()
            }
        }

        binding.placeOrder.setOnClickListener{
            if (!cartItemRefs.isNullOrEmpty()) {
                it.findNavController().navigate(ShoppingCartDirections.actionShoppingCartToCheckOut())
            }
        }

        return binding.root
    }

    private fun getBookData() {
        val db = Firebase.firestore

        cartItems.clear()
        externalAdapter.notifyDataSetChanged()

        if (!cartItemRefs.isNullOrEmpty()) {
            for (itemRef in cartItemRefs) {
                db.collection("books")
                    .document(itemRef)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val cartItem = CartItem(
                                document.id,
                                document.get("title") as String?,
                                document.get("description") as String?,
                                document.get("price") as Number?,
                                document.get("imageUrl") as String?
                            )
                            cartItems.add(cartItem)

                            externalAdapter.notifyDataSetChanged()
                            totalPrice += cartItem.price?.toDouble() ?: 0.00
                            binding.totalPriceAmount.text = String.format("RM %.2f", totalPrice)
                        } else {
                            Log.d("ShoppingCart", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("ShoppingCart", "get failed with ", exception)
                    }
            }
        }
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