package com.example.huaheejqc.sellerBookManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.huaheejqc.databinding.FragmentCompletedOrderBinding

class CompletedOrder : Fragment() {
    private var _binding: FragmentCompletedOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompletedOrderBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

}