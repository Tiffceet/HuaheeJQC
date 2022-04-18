package com.example.huaheejqc.sallerBookManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.huaheejqc.R
import com.example.huaheejqc.databinding.FragmentPostedItemBinding
import com.example.huaheejqc.databinding.FragmentToShipBinding

class PostedItem : Fragment() {
    private var _binding: FragmentPostedItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostedItemBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.posteditemAddBtn.setOnClickListener{view:View->
            view.findNavController().navigate(R.id.action_sellerBookManagement_to_addBook)
        }

        return view
    }

}