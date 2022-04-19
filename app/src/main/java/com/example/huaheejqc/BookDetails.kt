package com.example.huaheejqc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.huaheejqc.databinding.FragmentAddBookBinding
import com.example.huaheejqc.databinding.FragmentBookDetailsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookDetails : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentBookDetailsBinding? = null
    private val binding get() = _binding!!
    val args: BookDetailsArgs by navArgs()

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
        _binding = FragmentBookDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        val db = Firebase.firestore
        val bookid:String = args.viewbookid.toString()
        val userid = Firebase.auth.currentUser?.uid
        val stringID = userid.toString()
        Log.d("chin",args.viewbookid)

        val docRef = db.collection("books").document(bookid)
        docRef.get().addOnSuccessListener {document ->
            if (document != null) {
                Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                val title:String = document.get("title") as String
                val author:String = document.get("author") as String
                val price:Number = document.get("price") as Number
                val description:String = document.get("description") as String
                val priceString = "RM " + DecimalFormat("####.00").format(price).toString()

                binding.bookdetailsBooknameTxt.setText(title)
                binding.bookdetailsAuthorTxt.setText(author)
                binding.bookdetailsPriceTxt.setText(priceString)
                binding.bookdetailsDescriptionTxt.setText(description)
            } else {
                Log.d("TAG", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookDetails.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookDetails().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}