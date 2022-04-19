package com.example.huaheejqc

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.huaheejqc.data.User
import com.example.huaheejqc.databinding.FragmentLoginBinding
import com.example.huaheejqc.databinding.FragmentUserEditBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserEdit.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserEdit : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentUserEditBinding? = null
    private val binding get() = _binding!!

    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var storage: FirebaseStorage
    var userUploadedImg = false
    var uploadedImageBitmap: Bitmap? = null

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
        // Inflate the layout for this fragment
        _binding = FragmentUserEditBinding.inflate(inflater, container, false)
        val view = binding.root
        val db = Firebase.firestore
        val dbGet = FirebaseFirestore.getInstance()
        val userid = Firebase.auth.currentUser?.uid
        val stringID = userid.toString()

        val docRef = dbGet.collection("User").document(stringID)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("exist", "DocumentSnapshot data: ${document.data}")
                    val imageUrl = document.get("imageUrl") as String

                    if (document.getString("name") != null) {
                        binding.getNewName.setText(document.getString("name"))
                    } else {
                        binding.currentName.text = "Your data is not currently in the database"
                    }

                    if (document.getString("contact") != null) {
                        binding.getNewContact.setText(document.getString("contact"))
                    } else {
                        binding.currentContact.text = "Your data is not currently in the database"
                    }

                    if (document.getString("ic") != null) {
                        binding.getNewIc.setText(document.getString("ic"))
                    } else {
                        binding.currentIc.text = "Your data is not currently in the database"
                    }

                    if (document.getString("address") != null) {
                        binding.getNewAddress.setText(document.getString("address"))
                    } else {
                        binding.currentAddress.text = "Your data is not currently in the database"
                    }

                    if (document.getString("imageUrl") != null) {
                        binding.getNewContact.setText(document.getString("contact"))
                    } else {
                        binding.currentContact.text = "Your data is not currently in the database"
                    }

                    val storage = Firebase.storage
                    var storageRef = storage.reference
                    var imageRef = storageRef.child("images/${imageUrl}")
                    val localFile = File.createTempFile("images", "jpg")

                    imageRef.getFile(localFile).addOnSuccessListener {
                        val myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath())
                        binding.profileImage.setImageBitmap(myBitmap)
                        Log.d("BookdetailImage", "succ")
                    }.addOnFailureListener {
                        Log.d("noob", "noob")
                    }

                } else {
                    Log.d("errordbGet", "get failed")
                }

            }

        binding.startEditUserProfile.setOnClickListener { view: View ->
            val newName = binding.getNewName.text.toString()
            val newAddress = binding.getNewAddress.text.toString()
            val newContact = binding.getNewContact.text.toString()
            val newIC = binding.getNewIc.text.toString()

            if (newName.isEmpty()) {
                binding.getNameStatusText.text = "Name cannot be empty!"
                return@setOnClickListener
            }else
            {
                binding.getNameStatusText.text = ""
            }

            if (newAddress.isEmpty()) {
                binding.getAddressStatusText.text = "Address cannot be empty!"
                return@setOnClickListener
            }else{
                binding.getAddressStatusText.text = ""
            }

            if (newContact.isEmpty()) {
                binding.getContactStatusText.text = "Contact cannot be empty!"
                return@setOnClickListener
            }else{
                binding.getContactStatusText.text=""
            }

            if (newIC.isEmpty()) {
                binding.getICStatusText.text = "IC cannot be empty!"
                return@setOnClickListener
            }else{
                binding.getICStatusText.text = ""
            }

            if(binding.getNewIc.text.toString().length != 12 ) {
                binding.getICStatusText.text = "IC should be correct format which is 12 digit"
                return@setOnClickListener
            }else{
                binding.getICStatusText.text = ""
            }

            if(binding.getNewContact.text.toString().length == 10 || binding.getNewContact.text.toString().length == 11){
                if(newContact.substring(0, 1) != "0" && newContact.substring(1,2)!="1"){
                    binding.getContactStatusText.text = "Contact format should be 01XXXXXXXX"
                    return@setOnClickListener
                }
            }else{
                binding.getContactStatusText.text = "Contact length should be 10 until 11 digits only"
                return@setOnClickListener
            }

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

                val docRef = dbGet.collection("User").document(stringID)
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            var newAmount = document.get("amount") as Number
                            var intamount: Double = newAmount.toDouble()
                            var newEmail = document.getString("email")
                            var oldimageUrl = document.getString("imageUrl")

                            if(userUploadedImg==true){
                                db.collection("User").document(stringID).set(
                                    User(
                                        newName.toString(),
                                        newContact.toString(),
                                        newIC.toString(),
                                        newName.toString(),
                                        intamount,
                                        newEmail.toString(),
                                        imageUrl = timestamp.toString()
                                    )
                                )
                            }else{
                                db.collection("User").document(stringID).set(
                                    User(
                                        newName.toString(),
                                        newContact.toString(),
                                        newIC.toString(),
                                        newName.toString(),
                                        intamount,
                                        newEmail.toString(),
                                        oldimageUrl.toString()
                                    )
                                )
                            }


                            view.findNavController().navigateUp()
                        }
                    }
            }



        }

        binding.captureImage.setOnClickListener{ view: View ->
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadedImageBitmap = imageBitmap
            userUploadedImg = true
            binding.profileImage.setImageBitmap(imageBitmap)
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
         * @return A new instance of fragment UserEdit.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserEdit().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}