package com.panther.contentai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.panther.contentai.databinding.FragmentOnboardingScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class OnboardingScreen : Fragment() {
    private lateinit var binding :FragmentOnboardingScreenBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOnboardingScreenBinding.inflate(layoutInflater)

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
        binding.btnGetStarted.setOnClickListener {
            CuratorSharedPreference().updateSharedPref(false)
            val route = OnboardingScreenDirections.actionOnboardingScreenToSigninScreen()
            findNavController().navigate(route)
        }

    }

}