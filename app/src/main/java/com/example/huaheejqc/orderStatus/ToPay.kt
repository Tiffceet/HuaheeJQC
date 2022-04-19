package com.example.huaheejqc.orderStatus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.huaheejqc.R
import com.example.huaheejqc.databinding.FragmentToPayBinding

class ToPay : Fragment() {
    private var _binding: FragmentToPayBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentToPayBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.topayViewdetailsBtn.setOnClickListener{view:View->
//            view.findNavController().navigate(R.id.action_orderStatus2_to_bookDetails)
        }
        return view
    }
}