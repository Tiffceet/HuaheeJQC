package com.example.huaheejqc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.huaheejqc.data.User
import com.example.huaheejqc.databinding.FragmentReloadInUserWalletBinding
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
 * Use the [ReloadInUserWallet.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReloadInUserWallet : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentReloadInUserWalletBinding? = null
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
        _binding = FragmentReloadInUserWalletBinding.inflate(inflater, container, false)
        val view = binding.root
        val db = Firebase.firestore
        val dbGet = FirebaseFirestore.getInstance()
        val userid = Firebase.auth.currentUser?.uid
        val stringID = userid.toString()

        var reload_amount = 0
        var checkOtherButton = 0

        binding.reloadRm50.setOnClickListener{ view:View ->
            if(reload_amount != null){
                binding.ReloadPageStatusText.text = "Reload amount RM 50 selected unsuccessfully because above amount has been filled in."
                return@setOnClickListener
            }else{
                reload_amount = 50;
                binding.ReloadPageStatusText.text = "Reload amount RM 50 selected successfully."
                checkOtherButton = 1;
            }
        }

        binding.reloadRm100.setOnClickListener{ view:View ->
            if(binding.getReloadNumber.text != null){
                binding.ReloadPageStatusText.text = "Reload amount RM 100 selected unsuccessfully because above amount has been filled in."
                return@setOnClickListener
            }else{
                reload_amount = 100;
                checkOtherButton = 1;
                binding.ReloadPageStatusText.text = "Reload amount RM 100 selected successfully."
            }
        }

        binding.reloadRm150.setOnClickListener{ view:View ->
            if(binding.getReloadNumber.text != null){
                binding.ReloadPageStatusText.text = "Reload amount RM 150 selected unsuccessfully because above amount has been filled in."
                return@setOnClickListener
            }else{
                reload_amount = 150;
                checkOtherButton = 1;
                binding.ReloadPageStatusText.text = "Reload amount RM 150 selected successfully."
            }
        }

        binding.reloadRm200.setOnClickListener{ view:View ->
            if(binding.getReloadNumber.text != null){
                binding.ReloadPageStatusText.text = "Reload amount RM 200 selected unsuccessfully because above amount has been filled in."
                return@setOnClickListener
            }else{
                reload_amount = 200;
                checkOtherButton = 1;
                binding.ReloadPageStatusText.text = "Reload amount RM 200 selected successfully."
            }
        }

        binding.reloadRm250.setOnClickListener{ view:View ->
            if(binding.getReloadNumber.text != null){
                binding.ReloadPageStatusText.text = "Reload amount RM 250 selected unsuccessfully because above amount has been filled in."
                return@setOnClickListener
            }else{
                reload_amount = 250;
                checkOtherButton = 1;
                binding.ReloadPageStatusText.text = "Reload amount RM 250 selected successfully."
            }
        }

        binding.reloadRm300.setOnClickListener{ view:View ->
            if(binding.getReloadNumber.text != null){
                binding.ReloadPageStatusText.text = "Reload amount RM 300 selected unsuccessfully because above amount has been filled in."
                return@setOnClickListener
            }else{
                reload_amount = 300;
                checkOtherButton = 1;
                binding.ReloadPageStatusText.text = "Reload amount RM 300 selected successfully."
            }
        }

        binding.buttonStartReload.setOnClickListener { view: View ->

            var new_reload_amount = binding.getReloadNumber.text.toString()
            var int_reload_amount = new_reload_amount.toInt()
            var nummString = ""

            if (reload_amount == null) {
                binding.ReloadPageStatusText.text = "Reload Amount should not be empty!"
                return@setOnClickListener
            }

            val docRef = dbGet.collection("User").document(stringID)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        var name = document.getString("name")
                        var address = document.getString("address")
                        var ic = document.getString("ic")
                        var contact = document.getString("contact")
                        var amount = reload_amount

                        db.collection("User").document(stringID).set(
                            User(
                                address.toString(),
                                contact.toString(),
                                ic.toString(),
                                name.toString(),
                                amount
                            )
                        )

                    }
                    view.findNavController()
                        .navigate(R.id.action_reloadInUserWallet_to_user_wallet_management)
                }
        }

        return  view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReloadInUserWallet.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReloadInUserWallet().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}