package com.panther.contentai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.panther.contentai.databinding.FragmentEmailAuthScreenBinding


class EmailAuthScreen : Fragment() {
    private lateinit var emailBinding: FragmentEmailAuthScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        emailBinding = FragmentEmailAuthScreenBinding.inflate(layoutInflater)
        val view = emailBinding.root

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return emailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailBinding.signUpBtn.setOnClickListener {
            val route = EmailAuthScreenDirections.actionEmailAuthScreenToChatDest()
            findNavController().navigate(route)
        }
    }
}
