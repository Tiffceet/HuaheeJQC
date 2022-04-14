package com.example.huaheejqc

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.huaheejqc.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentLoginBinding? = null
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnLogin.setOnClickListener { view: View ->
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            if(email.isEmpty()) {
                binding.loginPageStatusText.text = "Email cannot be empty"
                return@setOnClickListener
            }
            if(password.isEmpty()) {
                binding.loginPageStatusText.text = "Password cannot be empty"
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity as Activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
//                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        binding.loginPageStatusText.setTextColor(Color.parseColor("#00FF00"))
                        binding.loginPageStatusText.text = "Login Successful"
                        view.findNavController().navigate(R.id.action_loginFragment_to_mainMenu)
                    } else {
                        // If sign in fails, display a message to the user.
                        binding.loginPageStatusText.text = "Wrong email/password"
                    }
                }
        }

        binding.btnRegister.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}