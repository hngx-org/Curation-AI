package com.panther.contentai.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.panther.contentai.databinding.FragmentSubscriptionBinding

class Subscription : Fragment() {
   private lateinit var binding:FragmentSubscriptionBinding
   //val checkoutViewModel = CheckOutViewModel(applicationContext)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubscriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }



        binding.subscribeBtn.setOnClickListener {  }
    }

}