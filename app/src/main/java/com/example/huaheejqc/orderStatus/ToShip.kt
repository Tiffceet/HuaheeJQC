package com.example.huaheejqc.orderStatus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.huaheejqc.R
import com.example.huaheejqc.databinding.FragmentToPayBinding
import com.example.huaheejqc.databinding.FragmentToReceiveBinding
import com.example.huaheejqc.databinding.FragmentToShipBinding

class ToShip : Fragment() {
    private var _binding: FragmentToShipBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentToShipBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }
}