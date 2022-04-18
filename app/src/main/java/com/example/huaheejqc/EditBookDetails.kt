package com.example.huaheejqc

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.huaheejqc.data.Book
import com.example.huaheejqc.databinding.FragmentAddBookBinding
import com.example.huaheejqc.databinding.FragmentEditBookDetailsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var dataArray:MutableList<Book> = ArrayList()

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
        dataArray=ArrayList()
        _binding = FragmentEditBookDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        val db = Firebase.firestore
        val bookid:String = args.editbook.toString()

        val docRef = db.collection("books").document(bookid)
            docRef.get().addOnSuccessListener {document ->
                if (document != null) {
                    Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                    val title:String = document.getString("title").toString()
                    val author:String = document.getString("author").toString()
                    val price:String = document.getString("price").toString()
                    val description:String = document.getString("description").toString()
                    val category:Int = document.getString("category").toString().toInt()
                    binding.editbookTitleTxt.setText(title)
                    binding.editbookAuthorTxt.setText(author)
                    binding.editbookPriceTxt.setText(price)
                    binding.editbookDescriptionTxt.setText(description)
                    binding.editbookCategorySpin.setSelection(category)
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }

        binding.editbookConfirmBtn.setOnClickListener{view: View ->
            val newTitle = binding.editbookTitleTxt.text.toString()
            val newAuthor = binding.editbookAuthorTxt.text.toString()
            val newPrice = binding.editbookPriceTxt.text.toString()
            val newDescription = binding.editbookDescriptionTxt.text.toString()
            val newCategory = binding.editbookCategorySpin.selectedItemPosition.toString()
            val userid = Firebase.auth.currentUser?.uid
            val stringID = userid.toString()
//            val bookArray = db.collection("user-book").document(stringID)
//            val list: ArrayList<String> = ArrayList()


        if (newTitle.isEmpty()) {
            binding.editbookTitleEro.text = "Title cannot be empty!"
            return@setOnClickListener
        }else{
            binding.editbookTitleEro.text = ""
        }
        if (newAuthor.isEmpty()) {
            binding.editbookAuthorEro.text = "Author cannot be empty!"
            return@setOnClickListener
        }else{
            binding.editbookAuthorEro.text = ""
        }
        if (newPrice.isEmpty()) {
            binding.editbookPriceEro.text = "Price cannot be empty!"
            return@setOnClickListener
        }else{
            binding.editbookPriceEro.text = ""
        }
        if (newDescription.isEmpty()) {
            binding.editbookDescriptionEro.text = "Description cannot be empty!"
            return@setOnClickListener
        }else{
            binding.editbookDescriptionEro.text = ""
        }
        if (newCategory.isEmpty()) {
            binding.editbookCategoryEro.text = "Description cannot be empty!"
            return@setOnClickListener
        }else{
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
        val book = Book(newTitle,newAuthor,newPrice,newDescription,newCategory,"Posted",stringID)

            db.collection("books").document(bookid)
                .set(book)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                    view.findNavController().navigateUp()}
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

    }

        // Inflate the layout for this fragment
        return view
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