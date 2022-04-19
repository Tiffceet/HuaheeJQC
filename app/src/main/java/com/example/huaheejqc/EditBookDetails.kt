package com.example.huaheejqc

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.huaheejqc.data.Book
import com.example.huaheejqc.databinding.FragmentAddBookBinding
import com.example.huaheejqc.databinding.FragmentEditBookDetailsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var dataArray: MutableList<Book> = ArrayList()

/**
 * A simple [Fragment] subclass.
 * Use the [EditBookDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditBookDetails : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentEditBookDetailsBinding? = null
    private val binding get() = _binding!!
    val args: EditBookDetailsArgs by navArgs()

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
        dataArray = ArrayList()
        _binding = FragmentEditBookDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        val db = Firebase.firestore
        val bookid: String = args.editbook.toString()

        val docRef = db.collection("books").document(bookid)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                val title: String = document.get("title") as String
                val author: String = document.get("author") as String
                val price: Number = document.get("price") as Number
                val description: String = document.get("description") as String
                val category: Number = document.get("category") as Number
                val status: String = document.get("status") as String
                binding.editbookTitleTxt.setText(title)
                binding.editbookAuthorTxt.setText(author)
                binding.editbookPriceTxt.setText(DecimalFormat("####.00").format(price))
                binding.editbookDescriptionTxt.setText(description)
                binding.editbookCategorySpin.setSelection(category.toInt())

                if (status == "PendingOrder") {
                    binding.editbookConfirmBtn.visibility = View.GONE
                    binding.editbookDeleteBtn.visibility = View.GONE
                    binding.editbookTitleTxt.isEnabled = false
                    binding.editbookAuthorTxt.isEnabled = false
                    binding.editbookPriceTxt.isEnabled = false
                    binding.editbookDescriptionTxt.isEnabled = false
                    binding.editbookCategorySpin.isEnabled = false
                    binding.editbookShippingBtn.visibility = View.VISIBLE
                    binding.editbookCancelBtn.visibility = View.VISIBLE
                } else if (status == "Posted") {
                    binding.editbookConfirmBtn.visibility = View.VISIBLE
                    binding.editbookDeleteBtn.visibility = View.VISIBLE
                    binding.editbookTitleTxt.isEnabled = true
                    binding.editbookAuthorTxt.isEnabled = true
                    binding.editbookPriceTxt.isEnabled = true
                    binding.editbookDescriptionTxt.isEnabled = true
                    binding.editbookCategorySpin.isEnabled = true
                    binding.editbookShippingBtn.visibility = View.GONE
                    binding.editbookCancelBtn.visibility = View.GONE
                } else {
                    binding.editbookConfirmBtn.visibility = View.GONE
                    binding.editbookDeleteBtn.visibility = View.GONE
                    binding.editbookTitleTxt.isEnabled = false
                    binding.editbookAuthorTxt.isEnabled = false
                    binding.editbookPriceTxt.isEnabled = false
                    binding.editbookDescriptionTxt.isEnabled = false
                    binding.editbookCategorySpin.isEnabled = false
                    binding.editbookShippingBtn.visibility = View.GONE
                    binding.editbookCancelBtn.visibility = View.GONE
                }
            } else {
                Log.d("TAG", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }



        binding.editbookConfirmBtn.setOnClickListener { view: View ->
            var confirmPrice: Number = 0
            val newTitle = binding.editbookTitleTxt.text.toString()
            val newAuthor = binding.editbookAuthorTxt.text.toString()
            val newPrice: String = binding.editbookPriceTxt.text.toString()
            val newDescription = binding.editbookDescriptionTxt.text.toString()
            val newCategory: Number =
                binding.editbookCategorySpin.selectedItemPosition.toString().toInt()
            val userid = Firebase.auth.currentUser?.uid
            val stringID = userid.toString()
//            val bookArray = db.collection("user-book").document(stringID)
//            val list: ArrayList<String> = ArrayList()


            if (newTitle.isEmpty()) {
                binding.editbookTitleEro.text = "Title cannot be empty!"
                return@setOnClickListener
            } else {
                binding.editbookTitleEro.text = ""
            }
            if (newAuthor.isEmpty()) {
                binding.editbookAuthorEro.text = "Author cannot be empty!"
                return@setOnClickListener
            } else {
                binding.editbookAuthorEro.text = ""
            }
            if (newPrice.isEmpty()) {
                binding.editbookPriceEro.text = "Price cannot be empty!"
                return@setOnClickListener
            } else {
                confirmPrice = DecimalFormat("####.00").format(
                    binding.editbookPriceTxt.text.toString().toDouble()
                ).toDouble()
                binding.editbookPriceEro.text = ""
            }
            if (newDescription.isEmpty()) {
                binding.editbookDescriptionEro.text = "Description cannot be empty!"
                return@setOnClickListener
            } else {
                binding.editbookDescriptionEro.text = ""
            }
            if (newCategory == null) {
                binding.editbookCategoryEro.text = "Description cannot be empty!"
                return@setOnClickListener
            } else {
                binding.editbookCategoryEro.text = ""
            }

//            val book = hashMapOf(
//                "Title" to newTitle,
//                "Author" to newAuthor,
//                "Price" to newPrice,
//                "Description" to newDescription,
//                "Category" to newCategory,
//                "Status" to "Posted"
//            )
            val book = Book(
                newTitle,
                newAuthor,
                confirmPrice,
                newDescription,
                newCategory,
                "Posted",
                stringID
            )

            db.collection("books").document(bookid)
                .set(book)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                    view.hideKeyboard()
                    view.findNavController().navigateUp()
                }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

        }

        binding.editbookDeleteBtn.setOnClickListener { view: View ->
            db.collection("books").document(bookid)
                .delete()
                .addOnSuccessListener {
                    Log.d("TAG", "DocumentSnapshot successfully deleted!")
                    view.findNavController().navigateUp()
                }
                .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
        }

        binding.editbookChangeimgBtn.setOnClickListener { view: View ->
            val action =
                EditBookDetailsDirections.actionEditBookDetailsToBookDetails(
                    bookid
                )
//                it.findNavController().navigate(R.id.action_chatFragment_to_conversationFragment)
            view.findNavController().navigate(action)
        }

        var buyerid = ""
        var orderid = ""
        binding.editbookShippingBtn.setOnClickListener { view: View ->
            db.collection("orders")
                .whereEqualTo("book", bookid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d("TAG", "${document.id} => ${document.data}")
                        orderid = document.id
                        buyerid = document.get("buyer") as String
                    }
                    db.collection("orders").document(orderid)
                        .update("status", "in_transit")
                        .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!")
                            db.collection("books").document(bookid)
                                .update("status","InTransit")
                                .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!")
                                    view.findNavController().navigateUp()
                                }
                                .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }
                        }
                        .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents: ", exception)
                }
           }
        // Inflate the layout for this fragment
        return view
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditBookDetails.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditBookDetails().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}