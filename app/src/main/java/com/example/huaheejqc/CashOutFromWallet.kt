package com.example.huaheejqc

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.huaheejqc.data.User
import com.example.huaheejqc.databinding.FragmentCashOutFromWalletBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CashOutFromWallet.newInstance] factory method to
 * create an instance of this fragment.
 */
class CashOutFromWallet : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentCashOutFromWalletBinding? = null
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
        _binding = FragmentCashOutFromWalletBinding.inflate(inflater, container, false)
        val view = binding.root
        val db = Firebase.firestore
        val dbGet = FirebaseFirestore.getInstance()
        val userid = Firebase.auth.currentUser?.uid
        val stringID = userid.toString()
        var check_on_click = 0

        binding.buttonToBank.setOnClickListener{view:View ->
            binding.methodStatusText.text = "Method has been selected"
            check_on_click = 1;
        }

        binding.buttonToOther.setOnClickListener{view:View ->
            binding.methodStatusText.text = "Method has been selected"
            check_on_click = 1;
        }

        binding.startCashOut.setOnClickListener { view: View ->
            if (check_on_click == 0) {
                binding.CashOutPageStatusText.text = "Please select a consumption method!"
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
                        var amount = document.get("amount") as Number
                        var addnewamount: Double = DecimalFormat("####.00").format(
                            binding.getReloadNumber.text.toString().toDouble()
                        ).toDouble()
                        var intamount: Double = amount.toDouble()
                        var cashoutamount:Double = 0.0
                        var email = document.getString("email")

                        if (addnewamount > intamount) {
                            binding.CashOutPageStatusText.text =
                                "Preferred Amount must be less than your wallet amount"
                            return@addOnSuccessListener
                        }

                        Log.d("test_addnewamount", addnewamount.toString())
                        Log.d("test_intamount", intamount.toString())
                        cashoutamount = intamount - addnewamount


                        db.collection("User").document(stringID).set(
                            User(
                                address.toString(),
                                contact.toString(),
                                ic.toString(),
                                name.toString(),
                                cashoutamount,
                                email.toString()
                            )
                        )

                    }
                }
            view.findNavController().navigateUp()
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
         * @return A new instance of fragment CashOutFromWallet.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CashOutFromWallet().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}