package com.panther.contentai

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.panther.contentai.databinding.FragmentSigninScreenBinding

class SigninScreen : Fragment() {
    private lateinit var binding : FragmentSigninScreenBinding
    private var exitAppToastStillShowing = false

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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            isEnabled = true
            exitApp()
        }
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

    private val exitAppTimer = object : CountDownTimer(2000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            exitAppToastStillShowing = false
        }
    }

    private fun exitApp() {
        if (exitAppToastStillShowing) {
            requireActivity().finish()
            return
        }

        Toast.makeText(this.requireContext(), "Tap again to exit", Toast.LENGTH_SHORT)
            .show()
        exitAppToastStillShowing = true
        exitAppTimer.start()
    }


}