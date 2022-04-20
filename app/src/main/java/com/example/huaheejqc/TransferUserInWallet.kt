package com.example.huaheejqc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.huaheejqc.data.User
import com.example.huaheejqc.databinding.FragmentTransferUserInWalletBinding
import com.example.huaheejqc.databinding.FragmentUserEditBinding
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
 * Use the [TransferUserInWallet.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransferUserInWallet : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentTransferUserInWalletBinding? = null
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
        _binding = FragmentTransferUserInWalletBinding.inflate(inflater, container, false)
        val view = binding.root
        val db = Firebase.firestore
        val dbGet = FirebaseFirestore.getInstance()
        val userid = Firebase.auth.currentUser?.uid
        val stringID = userid.toString()

        binding.transferStart.setOnClickListener { view: View ->
            if (binding.getTransferName.text.toString() == "" || binding.getTransferAmount.text.toString() == "") {
                binding.statusTransfer.text =
                    "All form should not be empty. Please try fill all the form data again."
                return@setOnClickListener
            }

            var get_target_email = binding.getTransferName.text.toString()
            var reload_amount: Double = 0.0
            var get_transfer_amount: Double = DecimalFormat("####.00").format(
                binding.getTransferAmount.text.toString().toDouble()
            ).toDouble()

            // Create a reference to the cities collection
            val citiesRef = db.collection("User")

            // Create a query against the collection.
            val query = citiesRef.whereEqualTo("email", get_target_email)
            var targetId = ""

            db.collection("User")
                .whereEqualTo("email", get_target_email)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty()) {
                        binding.statusTransfer.text =
                            "Target Email's User is not exist. Please try again."
                        return@addOnSuccessListener
                    } else {
                        val docRef = dbGet.collection("User").document(stringID)
                        docRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    var name = document.getString("name")
                                    var address = document.getString("address")
                                    var ic = document.getString("ic")
                                    var contact = document.getString("contact")
                                    var cashOutAmount = document.get("amount") as Number
                                    var intOutamount: Double = cashOutAmount.toDouble()
                                    var cashoutamount: Double = 0.0
                                    var email = document.getString("email")
                                    var imageUrl = document.getString("imageUrl")

                                    if (binding.getTransferAmount.text.toString()
                                            .toDouble() > intOutamount
                                    ) {
                                        binding.statusTransfer.text =
                                            "Preferred Amount must be less than your wallet amount"
                                        return@addOnSuccessListener
                                    }

                                    Log.d("test_addnewamount", get_transfer_amount.toString())
                                    Log.d("test_intamount", intOutamount.toString())


                                    cashoutamount = intOutamount - get_transfer_amount

                                    db.collection("User").document(stringID).set(
                                        User(
                                            address.toString(),
                                            contact.toString(),
                                            ic.toString(),
                                            name.toString(),
                                            cashoutamount,
                                            email.toString(),
                                            imageUrl.toString()
                                        )
                                    )
                                }
                            }
                    }
                    for (document in documents) {
                        targetId = document.id
                        var name = document.get("name") as String
                        var address = document.get("address") as String
                        var ic = document.get("ic") as String
                        var contact = document.get("contact") as String
                        var amount = document.get("amount") as Number
                        var intamount: Double = amount.toDouble()
                        var email = document.get("email") as String
                        var imageUrl = document.get("imageUrl") as String

                        reload_amount = intamount + get_transfer_amount

                        Log.d("testing_target_amount", reload_amount.toString())
                        Log.d("name", name.toString())

                        db.collection("User").document(targetId).set(
                            User(
                                address.toString(),
                                contact.toString(),
                                ic.toString(),
                                name.toString(),
                                reload_amount,
                                email.toString(),
                                imageUrl.toString()
                            )
                        ).addOnSuccessListener {
                            view.findNavController().navigateUp()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    binding.statusTransfer.text =
                        "Target Email's User is not exist. Please try again."
                    return@addOnFailureListener
        }

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
     * @return A new instance of fragment TransferUserInWallet.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(param1: String, param2: String) =
        TransferUserInWallet().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
}
//return view
}
