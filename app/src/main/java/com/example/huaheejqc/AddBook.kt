package com.example.huaheejqc

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.findNavController
import com.example.huaheejqc.data.Book
import com.example.huaheejqc.databinding.FragmentAddBookBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddBook.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddBook : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentAddBookBinding? = null
    private val binding get() = _binding!!

    private lateinit var button:Button
    private lateinit var inmageView: ImageView

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
        _binding = FragmentAddBookBinding.inflate(inflater, container, false)
        val view = binding.root
        val db = Firebase.firestore
        // Inflate the layout for this fragment

        db.collection("books")
            .whereEqualTo("userid", "123")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("TAG", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }



        binding.addbookConfirmBtn.setOnClickListener { view: View ->
            val newTitle = binding.addbookTitleTxt.text.toString()
            val newAuthor = binding.addbookAuthorTxt.text.toString()
            val newPrice = binding.addbookPriceTxt.text.toString()
            val newDescription = binding.addbookDescriptionTxt.text.toString()
            val newCategory = binding.addbookCategorySpin.selectedItem.toString()
            val userid = Firebase.auth.currentUser?.uid
            val stringID = userid.toString()
//            val bookArray = db.collection("user-book").document(stringID)
//            val list: ArrayList<String> = ArrayList()


            if (newTitle.isEmpty()) {
                binding.addbookTitleEro.text = "Title cannot be empty!"
                return@setOnClickListener
            }else{
                binding.addbookTitleEro.text = ""
            }
            if (newAuthor.isEmpty()) {
                binding.addbookAuthorEro.text = "Author cannot be empty!"
                return@setOnClickListener
            }else{
                binding.addbookAuthorEro.text = ""
            }
            if (newPrice.isEmpty()) {
                binding.addbookPriceEro.text = "Price cannot be empty!"
                return@setOnClickListener
            }else{
                binding.addbookPriceEro.text = ""
            }
            if (newDescription.isEmpty()) {
                binding.addbookDescriptionEro.text = "Description cannot be empty!"
                return@setOnClickListener
            }else{
                binding.addbookDescriptionEro.text = ""
            }
            if (newCategory.isEmpty()) {
                binding.addbookCategoryEro.text = "Description cannot be empty!"
                return@setOnClickListener
            }else{
                binding.addbookCategoryEro.text = ""
            }

//            val book = hashMapOf(
//                "Title" to newTitle,
//                "Author" to newAuthor,
//                "Price" to newPrice,
//                "Description" to newDescription,
//                "Category" to newCategory,
//                "Status" to "Posted"
//            )
           val book = Book(newTitle,newAuthor,newPrice,newDescription,newCategory,"Posted",stringID)

            db.collection("books")
                .add(book)
                .addOnSuccessListener { documentReference ->
                    Log.d("TAG", "DocumentSnapshot written with ID: ${documentReference.id}")
                    view.findNavController().navigateUp()
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding document", e)
                }

        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddBook.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddBook().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}