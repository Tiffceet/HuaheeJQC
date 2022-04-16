package com.example.huaheejqc

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.huaheejqc.databinding.FragmentReloadInWalletBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReloadInWallet.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReloadInWallet : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentReloadInWalletBinding
    private lateinit var db: FirebaseFirestore
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
        // Inflate the layout for this fragment
        auth = Firebase.auth
        binding = FragmentReloadInWalletBinding.inflate(layoutInflater)
        val view = binding.root
        db = FirebaseFirestore.getInstance()

        binding.buttonStartReload.setOnClickListener{
            val reload_amount = binding.getReloadNumber.toString()
            var int_reload_amount = reload_amount.toInt()
            var checkOtherButton = 0;

            if(binding.reloadRm50.hasOnClickListeners()){
                int_reload_amount = 50;
                binding.ReloadPageStatusText.text = "Reload amount RM 50 selected successfully."
                checkOtherButton = 1;
            }else if(binding.reloadRm100.hasOnClickListeners()){
                int_reload_amount = 100;
                checkOtherButton = 1;
                binding.ReloadPageStatusText.text = "Reload amount RM 100 selected successfully."
            }else if(binding.reloadRm150.hasOnClickListeners()){
                int_reload_amount = 150;
                checkOtherButton = 1;
                binding.ReloadPageStatusText.text = "Reload amount RM 150 selected successfully."
            }else if(binding.reloadRm200.hasOnClickListeners()){
                int_reload_amount = 200;
                checkOtherButton = 1;
                binding.ReloadPageStatusText.text = "Reload amount RM 200 selected successfully."
            }else if(binding.reloadRm250.hasOnClickListeners()){
                int_reload_amount = 250;
                checkOtherButton = 1;
                binding.ReloadPageStatusText.text = "Reload amount RM 250 selected successfully."
            }else if(binding.reloadRm300.hasOnClickListeners()){
                int_reload_amount = 300;
                checkOtherButton = 1;
                binding.ReloadPageStatusText.text = "Reload amount RM 300 selected successfully."
            }

            if(reload_amount.isEmpty() && checkOtherButton == 0){
                binding.ReloadPageStatusText.text = "Reload Amount should not be empty!"
                return@setOnClickListener
            }

            val user = User("zhexue",int_reload_amount)

            db.collection("User").document("IeewpA3XXVfBjcVPUqpc").set(user)
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
         * @return A new instance of fragment ReloadInWallet.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReloadInWallet().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}