package com.example.huaheejqc

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.findNavController
import com.example.huaheejqc.data.Feedback
import com.example.huaheejqc.databinding.FragmentFeedbackBinding
import com.example.huaheejqc.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FeedbackFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedbackFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!
    private val PICK_IMAGE_REQUEST = 71
    private lateinit var uploadedImageUri: Uri
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

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
        storage = Firebase.storage

        db = Firebase.firestore
        auth = Firebase.auth
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.uploadImageButton.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST
            )
        }

        binding.submitFeedbackBtn.setOnClickListener {
            val issue = binding.issueTextView.text.toString()
            val contactEmail = binding.contactEmailEditText.text.toString()
            val uid = auth.currentUser?.uid.toString()
            if (this::uploadedImageUri.isInitialized) {
                var storageRef = storage.reference
                val timestamp = System.currentTimeMillis() / 1000L
                var imageRef = storageRef.child("images/${timestamp.toString()}")
                imageRef.putFile(uploadedImageUri).addOnFailureListener {
                    Log.d("FirebaseStorage", "Failed")
                    binding.root.hideKeyboard()
                }.addOnSuccessListener { taskSnapshot ->
                    val feedbackObj = Feedback(issue, contactEmail, uid, timestamp.toString())
                    db.collection("feedbacks").add(feedbackObj)
                    Log.d("FirebaseStorage", "Success")
                    binding.root.hideKeyboard()
                    it.findNavController().navigate(R.id.action_feedbackFragment_to_feedbackSuccessulFragment)
                }
            } else {
                val feedbackObj = Feedback(issue, contactEmail, uid, "")
                db.collection("feedbacks").add(feedbackObj)
                binding.root.hideKeyboard()
                it.findNavController().navigate(R.id.action_feedbackFragment_to_feedbackSuccessulFragment)
            }

        }
        return view
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            val filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, filePath)
                binding.uploadedImageView.setImageBitmap(bitmap)
                binding.uploadedImageView.visibility = View.VISIBLE
                uploadedImageUri = filePath!!
            } catch (e: IOException) {
                e.printStackTrace()
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
         * @return A new instance of fragment FeedbackFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FeedbackFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}