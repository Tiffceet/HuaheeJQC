package com.example.huaheejqc

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.huaheejqc.data.User
import com.example.huaheejqc.databinding.FragmentLoginBinding
import com.example.huaheejqc.databinding.FragmentUserEditBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
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
        val dbGet = FirebaseFirestore.getInstance()
        val userid = Firebase.auth.currentUser?.uid
        val stringID = userid.toString()

        val docRef = dbGet.collection("User").document(stringID)
        docRef.get()
            .addOnSuccessListener {document ->
                if(document != null){
                    Log.d("exist","DocumentSnapshot data: ${document.data}")

                    if(document.getString("name") != null){
                        binding.currentName.text = document.getString("name")
                    }else{
                        binding.currentName.text = "Your data is not currently in the database"
                    }

                    if(document.getString("contact") != null){
                        binding.currentContact.text = document.getString("contact")
                    }else{
                        binding.currentContact.text = "Your data is not currently in the database"
                    }

                    if(document.getString("ic") != null){
                        binding.currentIc.text = document.getString("ic")
                    }else{
                        binding.currentIc.text = "Your data is not currently in the database"
                    }

                    if(document.getString("address") != null){
                        binding.currentAddress.text = document.getString("address")
                    }else{
                        binding.currentAddress.text = "Your data is not currently in the database"
                    }

                }else{
                    Log.d("errordbGet","get failed")
                }

            }

        binding.startEditUserProfile.setOnClickListener{ view: View ->
            val newName = binding.getNewName.text.toString()
            val newAddress = binding.getNewAddress.text.toString()
            val newContact = binding.getNewContact.text.toString()
            val newIC = binding.getNewIc.text.toString()

            if(newName.isEmpty()){
                binding.getNameStatusText.text = "Name cannot be empty!"
                return@setOnClickListener
            }
            if(newAddress.isEmpty()){
                binding.getAddressStatusText.text = "Address cannot be empty!"
                return@setOnClickListener
            }
            if(newContact.isEmpty()){
                binding.getContactStatusText.text = "Contact cannot be empty!"
                return@setOnClickListener
            }
            if(newIC.isEmpty()){
                binding.getICStatusText.text = "IC cannot be empty!"
                return@setOnClickListener
            }


            val docRef = dbGet.collection("User").document(stringID)
            docRef.get()
                .addOnSuccessListener {document ->
                    if(document!=null){
                        var newAmount = document.get("amount") as Number
                        var intamount:Double = newAmount.toDouble()
                        var newEmail = document.getString("email")

                        db.collection("User").document(stringID).set(
                            User(
                                newName.toString(),
                                newContact.toString(),
                                newIC.toString(),
                                newName.toString(),
                                intamount,
                                newEmail.toString()
                            )
                        )
                    }
                    view.findNavController().navigateUp()}
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