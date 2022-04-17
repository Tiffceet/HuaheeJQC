package com.example.huaheejqc

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.huaheejqc.databinding.FragmentLoginBinding
import com.example.huaheejqc.databinding.FragmentUserEditBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

        binding.startEditUserProfile.setOnClickListener{ view: View ->
            val newEmail = binding.getNewEmail.text.toString()
            val newPassword = binding.getNewPassword.text.toString()
            val confirmPassword = binding.confirmNewPassword.text.toString()
            val newName = binding.getNewName.text.toString()
            val newAddress = binding.getNewAddress.text.toString()
            val newContact = binding.getNewContact.text.toString()
            val newIC = binding.getNewIc.text.toString()
            val userid = Firebase.auth.currentUser?.uid
            val stringID = userid.toString()

            if(newEmail.isEmpty()){
                binding.getEmailStatusText.text = "Email cannot be empty!"
                return@setOnClickListener
            }
            if(newPassword.isEmpty()){
                binding.getEmailStatusText.text = "Password cannot be empty!"
                return@setOnClickListener
            }
            if(newName.isEmpty()){
                binding.getEmailStatusText.text = "Name cannot be empty!"
                return@setOnClickListener
            }
            if(newAddress.isEmpty()){
                binding.getEmailStatusText.text = "Address cannot be empty!"
                return@setOnClickListener
            }
            if(newContact.isEmpty()){
                binding.getEmailStatusText.text = "Contact cannot be empty!"
                return@setOnClickListener
            }
            if(newIC.isEmpty()){
                binding.getEmailStatusText.text = "IC cannot be empty!"
                return@setOnClickListener
            }
            if(confirmPassword != newPassword){
                binding.confirmPasswordStatusText.text = "Password and Confirm Password should be same!"
                return@setOnClickListener
            }
/*
            val newUser = hashMapOf(
                "Name" to newName,
                "Email" to newEmail,
                "Password" to newPassword,
                "Address" to newAddress,
                "IC" to newIC,
                "Contact" to newContact
            )

            db.collection("User").document(stringID)
                .set(newUser)
                .addOnSuccessListener { }
                .addOnFailureListener { }
            */

            val city = hashMapOf(
                "Name" to newName,
                "Email" to newEmail,
                "Password" to newPassword,
                "Address" to newAddress,
                "IC" to newIC,
                "Contact" to newContact
            )

            db.collection("User").document(stringID)
                .set(city)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

            view.findNavController().navigate(R.id.action_userEdit_to_userProfileManagement)
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