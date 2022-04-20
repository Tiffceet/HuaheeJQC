package com.example.huaheejqc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huaheejqc.data.Book
import com.example.huaheejqc.data.SearchVal
import com.example.huaheejqc.databinding.FragmentMainMenuBinding
import com.example.huaheejqc.databinding.FragmentSearchBinding
import com.example.huaheejqc.mainMenu.MenuShowcasesAdapter
import com.example.huaheejqc.search.SearchAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainMenu.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainMenu : Fragment() {
    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!
    private var allBooks:ArrayList<Book> = ArrayList()
    private var allBooks2:ArrayList<Book> = ArrayList()
    private lateinit var externalAdapter: MenuShowcasesAdapter
    private lateinit var alsoLikeAdapter: MenuShowcasesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allBooks = ArrayList()
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        val db = Firebase.firestore
        db.collection("books").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val book = Book(
                    document.get("title") as String,
                    document.get("author") as String,
                    document.get("price") as Number,
                    document.get("description") as String,
                    document.get("page_amount") as Number,
                    document.get("category") as Number,
                    document.get("status") as String,
                    document.get("userid") as String,
                    document.id,
                    document.get("imageUrl") as String,
                )

                if (book.status.toString() == "Posted") {
                    allBooks.add(book)
                    externalAdapter.notifyItemInserted(allBooks.size - 1)
                }
            }
        }

        externalAdapter = MenuShowcasesAdapter(allBooks)
        binding.newReleaseList.adapter = externalAdapter
        val pageAmountLayout = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.newReleaseList.layoutManager = pageAmountLayout

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainMenu.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainMenu().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}