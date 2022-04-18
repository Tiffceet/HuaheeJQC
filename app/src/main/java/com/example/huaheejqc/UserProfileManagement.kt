package com.example.huaheejqc

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import com.example.huaheejqc.databinding.FragmentLoginBinding
import com.example.huaheejqc.databinding.FragmentUserProfileManagementBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserProfileManagement.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserProfileManagement : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentUserProfileManagementBinding? = null
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
        _binding = FragmentUserProfileManagementBinding.inflate(inflater, container, false)
        val view = binding.root
        val db = FirebaseFirestore.getInstance()
        val userid = Firebase.auth.currentUser?.uid
        val stringID = userid.toString()

        val docRef = db.collection("User").document(stringID)
        docRef.get()
            .addOnSuccessListener {document ->
                if(document != null){
                    Log.d("exist","DocumentSnapshot data: ${document.data}")

                    binding.profileName.text = document.getString("Name")
                    binding.profileEmail.text = document.getString("Contact")
                }else{
                    Log.d("errordb","get failed with ")
                }

        }

        binding.goToEditUser.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_userProfileManagement_to_userEdit)
        }

        binding.goToBookManagement.setOnClickListener{view:View ->
            view.findNavController().navigate(R.id.action_userProfileManagement_to_sellerBookManagement)
        }

        binding.buttonToOrderDetails.setOnClickListener{view:View->
            view.findNavController().navigate(R.id.action_userProfileManagement_to_orderStatus2)
        }

        binding.btnToLogOut.setOnClickListener{view:View->
            Firebase.auth.signOut()
            view.findNavController().navigate(R.id.action_userProfileManagement_to_loginFragment)
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
         * @return A new instance of fragment UserProfileManagement.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserProfileManagement().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}