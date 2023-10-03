package com.panther.contentai.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.panther.contentai.R
import com.panther.contentai.databinding.FragmentHistoryBinding

class History : Fragment() {
    private lateinit var binding:FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            isEnabled = true
            val route = HistoryDirections.actionHistoryToChatDest()
            findNavController().navigate(route)
        }

        changeSearchViewPlate()
        binding.backBtn.setOnClickListener {
            val route = HistoryDirections.actionHistoryToChatDest()
            findNavController().navigate(route)
        }
    }


    private fun changeSearchViewPlate(){
        val searchPlate = binding.searchBar.findViewById<View>(androidx.appcompat.R.id.search_plate)
        searchPlate.setBackgroundResource(R.drawable.transparent_background)
    }


}