package com.panther.contentai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.panther.contentai.databinding.FragmentSigninScreenBinding

class SigninScreen : Fragment() {
    private lateinit var binding : FragmentSigninScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSigninScreenBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emailBtn.setOnClickListener {
            val route = SigninScreenDirections.actionSigninScreenToEmailAuthScreen()
            findNavController().navigate(route)
        }
        binding.btnGoogleSignUp.setOnClickListener {
            val route = SigninScreenDirections.actionSigninScreenToEmailAuthScreen()
            findNavController().navigate(route)
        }
        binding.loginText.setOnClickListener {
            val route = SigninScreenDirections.actionSigninScreenToLogIn()
            findNavController().navigate(route)
        }
    }


}