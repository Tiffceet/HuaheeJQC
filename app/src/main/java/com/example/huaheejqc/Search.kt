package com.example.huaheejqc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huaheejqc.data.SearchVal
import com.example.huaheejqc.databinding.FragmentSearchBinding
import com.example.huaheejqc.search.SearchAdapter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Search.newInstance] factory method to
 * create an instance of this fragment.
 */
class Search : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var pageAmountSearchVals:ArrayList<SearchVal> = ArrayList()
    private var classifySearchVals:ArrayList<SearchVal> = ArrayList()

    private lateinit var pageAmountAdapter: SearchAdapter
    private lateinit var classifyAdapter: SearchAdapter

    var selectedTags = HashMap<String, SearchVal?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val db = Firebase.firestore

        pageAmountSearchVals.add(SearchVal("page_amount", "equals", "Default", null))
        classifySearchVals.add(SearchVal("classification", "equals", "Default", null))

        db.collection("searches")
            .whereEqualTo("category", "page_amount")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document != null) {
                        val searchValData = SearchVal(
                            document.get("category") as String?,
                            document.get("condition_type") as String?,
                            document.get("display") as String?,
                            document.get("value")
                        )

                        pageAmountSearchVals.add(searchValData)
                        pageAmountAdapter.notifyItemInserted(pageAmountSearchVals.size - 1)
                        pageAmountSearchVals.sortBy { it.display }
                    }
                }
            }

        pageAmountAdapter = SearchAdapter(this, pageAmountSearchVals)
        binding.pageAmountList.adapter = pageAmountAdapter
        val pageAmountLayout = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.pageAmountList.layoutManager = pageAmountLayout

        db.collection("searches")
            .whereEqualTo("category", "classification")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document != null) {
                        val searchValData = SearchVal(
                            document.get("category") as String?,
                            document.get("condition_type") as String?,
                            document.get("display") as String?,
                            document.get("value")
                        )

                        classifySearchVals.add(searchValData)
                        classifyAdapter.notifyItemInserted(classifySearchVals.size - 1)
                        classifySearchVals.sortBy { it.value.toString() }
                    }
                }
            }

        classifyAdapter = SearchAdapter(this, classifySearchVals)
        binding.classificationList.adapter = classifyAdapter
        val classifyLayout = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.classificationList.layoutManager = classifyLayout

        binding.searchButton.setOnClickListener({
            clickSearch()
        })

        return binding.root
    }

    private fun performSearch(query: String? = "") {

    }

    fun clickSearch() {
        val db = Firebase.firestore
        val pageAmount = selectedTags["page_amount"]
        val classify = selectedTags["classification"]
        val searchText = binding.searchInput.text.toString()

        var dbQuery: Query = db.collection("books")

        if (searchText.isNotEmpty()) {
            val query = searchText.trim()

            dbQuery = dbQuery.whereGreaterThanOrEqualTo("title", query)
                .whereLessThanOrEqualTo("title", query + "\\uf8ff")
        }

        Log.d("ShoppingCart", selectedTags["equals"].toString())

        if (pageAmount != null) {
            if (pageAmount.conditionType == "more_than") {
                dbQuery = pageAmount.value?.let { dbQuery.whereGreaterThan("page_amount", it) }!!
            } else if (pageAmount.conditionType == "less_than") {
                dbQuery = pageAmount.value?.let { dbQuery.whereLessThan("page_amount", it) }!!
            } else {
                dbQuery = pageAmount.value?.let { dbQuery.whereEqualTo("page_amount", it) }!!
            }
        }

        if (classify != null) {
            dbQuery = dbQuery.whereEqualTo("category", classify.value)
        }

        dbQuery.get().addOnSuccessListener { documents ->
            for (document in documents) {
                Log.d("ShoppingCart", document.data.toString())
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Search.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Search().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}