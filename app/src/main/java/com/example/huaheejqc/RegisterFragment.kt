package com.example.huaheejqc

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.huaheejqc.data.User
import com.example.huaheejqc.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
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
        auth = Firebase.auth
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val rootView = binding.root
        binding.registerBtn.setOnClickListener { view: View ->
            val email: String = binding.inputEmail.text.toString()
            val password: String = binding.inputPassword.text.toString()
            val confirmPW: String = binding.inputConfirmPassword.text.toString()
            if (password != confirmPW) {
                binding.statusText.text = "Password do not match.";
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.statusText.text = "Email cannot be empty";
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.statusText.text = "Password cannot be empty";
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.statusText.text = "Password length needs to be at least 6 character long";
                return@setOnClickListener
            }

            Log.d("Debug", email)
            Log.d("Debug", password)
            Log.d("Debug", confirmPW)
            val firestore = Firebase.firestore
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity as Activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        val uid = user?.uid.toString()
                        binding.statusText.setTextColor(Color.parseColor("#00FF00"))
                        binding.statusText.text = "Successfully Registered";
                        firestore.collection("User").document(uid).set(User("", "", "", "",0.00))
                        view.findNavController().navigateUp()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("Debug", task.exception.toString())
                        binding.statusText.text = "Failed to register." + task.exception?.message;
                    }
                }
        }
        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}