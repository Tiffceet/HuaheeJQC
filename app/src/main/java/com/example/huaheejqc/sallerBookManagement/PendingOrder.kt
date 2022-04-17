package com.example.huaheejqc.sallerBookManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.huaheejqc.R
import com.example.huaheejqc.databinding.FragmentPendingOrderBinding
import com.example.huaheejqc.databinding.FragmentToShipBinding

class PendingOrder : Fragment() {
    private var _binding: FragmentPendingOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPendingOrderBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

}