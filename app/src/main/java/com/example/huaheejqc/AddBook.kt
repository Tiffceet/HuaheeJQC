package com.example.huaheejqc

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.findNavController
import com.example.huaheejqc.data.Book
import com.example.huaheejqc.databinding.FragmentAddBookBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat

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
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var storage:FirebaseStorage
    var userUploadedImg = false
    var uploadedImageBitmap:Bitmap? = null

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
        userUploadedImg = false
        storage=Firebase.storage

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
            var confirmPrice:Number = 0;
            var confirmPageAmount = 0;
            val newTitle = binding.addbookTitleTxt.text.toString()
            val newAuthor = binding.addbookAuthorTxt.text.toString()
            var newPrice = binding.addbookPriceTxt.text.toString()
            var newPageAmount = binding.addbookPageamountTxt.text.toString()
            val newDescription = binding.addbookDescriptionTxt.text.toString()
            val newCategory:Number = binding.addbookCategorySpin.selectedItemPosition.toString().toInt()
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
                confirmPrice = DecimalFormat("####.00").format(binding.addbookPriceTxt.text.toString().toDouble()).toDouble()
                binding.addbookPriceEro.text = ""
            }
            if (newPageAmount.isEmpty()) {
                binding.addbookPriceEro.text = "Price cannot be empty!"
                return@setOnClickListener
            }else{
                confirmPageAmount = binding.addbookPageamountTxt.text.toString().toInt()
                binding.addbookPageamountEro.text = ""
            }
            if (newDescription.isEmpty()) {
                binding.addbookDescriptionEro.text = "Description cannot be empty!"
                return@setOnClickListener
            }else{
                binding.addbookDescriptionEro.text = ""
            }
            if (newCategory == null) {
                binding.addbookCategoryEro.text = "Description cannot be empty!"
                return@setOnClickListener
            }else{
                binding.addbookCategoryEro.text = ""
            }
            if (userUploadedImg == false) {
                binding.addbookImgEro.text = "Must Upload Book Image"
                return@setOnClickListener
            }else{
                binding.addbookImgEro.text = ""
            }

//            val book = hashMapOf(
//                "Title" to newTitle,
//                "Author" to newAuthor,
//                "Price" to newPrice,
//                "Description" to newDescription,
//                "Category" to newCategory,
//                "Status" to "Posted"
//            )

            val baos = ByteArrayOutputStream()
            uploadedImageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val storage = Firebase.storage
            var storageRef = storage.reference
            val timestamp = System.currentTimeMillis() / 1000L
            var imageRef = storageRef.child("images/${timestamp.toString()}")
            var uploadTask = imageRef.putBytes(data)
            uploadTask.addOnFailureListener {
                Log.d("Succ", "noob")
            }.addOnSuccessListener { taskSnapshot ->
                Log.d("Succ", "yay")

                val book = Book(newTitle,newAuthor,confirmPrice,newDescription,confirmPageAmount,newCategory,"Posted",stringID, imageUrl = timestamp.toString())

                db.collection("books")
                    .add(book)
                    .addOnSuccessListener { documentReference ->
                        Log.d("TAG", "DocumentSnapshot written with ID: ${documentReference.id}")
                        view.hideKeyboard()
                        view.findNavController().navigateUp()
                    }
                    .addOnFailureListener { e ->
                        Log.w("TAG", "Error adding document", e)
                    }
            }
        }

        binding.addbookAddimgBtn.setOnClickListener{ view: View ->
            Log.d("test","askjd")
            dispatchTakePictureIntent()
        }
        return view
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadedImageBitmap = imageBitmap
            userUploadedImg = true
            binding.addbookImg.setImageBitmap(imageBitmap)
        }
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