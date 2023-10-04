package com.panther.contentai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.content.Context
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope

import androidx.navigation.fragment.findNavController
import com.panther.contentai.arch_comp.CuratorViewModel
import com.panther.contentai.databinding.FragmentEmailAuthScreenBinding
import com.panther.contentai.util.Resource
import com.shegs.hng_auth_library.authlibrary.AuthLibrary
import com.shegs.hng_auth_library.model.AuthResponse
import com.shegs.hng_auth_library.model.SignupRequest
import com.shegs.hng_auth_library.network.ApiResponse
import com.shegs.hng_auth_library.repositories.SignupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EmailAuthScreen : Fragment() {
    private lateinit var emailBinding: FragmentEmailAuthScreenBinding
    private val curatorViewModel by activityViewModels<CuratorViewModel>()

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
       /* val apiService = AuthLibrary.createAuthService()
        val dataStoreRepository = AuthLibrary.createDataStoreRepository(requireContext())
        val signupRepository = AuthLibrary.createSignupRepository(apiService)

        val signupRequest = SignupRequest(
            name = "John Doe",
            email = "johndoe@example.com",
            password = "password123",
            confirm_password = "password123"
        )*/


        emailBinding.signUpBtn.setOnClickListener {
            val name = emailBinding.editTextName.text.toString()
            val email = emailBinding.editTextEmail.text.toString()
            val password = emailBinding.editTextPassword.text.toString()
            val confirmPassword = emailBinding.editTextConfirmPassword.text.toString()

            if (name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty() ){
                Toast.makeText(requireContext(), "Field cannot be empty", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords must match", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            curatorViewModel.signUpUser(name, email, password, confirmPassword)
            observeSignUpState()
        }
    }



    private fun observeSignUpState() {
        lifecycleScope.launch {
            curatorViewModel.userDataState.collect { state ->
                emailBinding.apply {
                    when (state) {

                        is Resource.Loading -> {
                            progressBar.isVisible = true
                        }

                        is Resource.Successful -> {
                            progressBar.isVisible = false
                            val route =
                                EmailAuthScreenDirections.actionEmailAuthScreenToChatDest()
                            findNavController().navigate(route)
                        }

                        is Resource.Failure -> {
                            progressBar.isVisible = false
                            Toast.makeText(requireContext(), state.msg, Toast.LENGTH_SHORT)
                                .show()

                        }
                    }
                }
            }
        }
    }
}

/*
private fun signUpUser(signupRepository:SignupRepository) {
    val name = emailBinding.editTextName.text.toString()
    val email = emailBinding.editTextEmail.text.toString()
    val password = emailBinding.editTextPassword.text.toString()
    val confirmPassword = emailBinding.editTextConfirmPassword.text.toString()

    if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ){
        Toast.makeText(requireContext(), "Field cannot be empty", Toast.LENGTH_SHORT)
            .show()
        return
    }

    if (password != confirmPassword) {
        Toast.makeText(requireContext(), "Passwords must match", Toast.LENGTH_SHORT)
            .show()
        return
    }

    val signupRequest = SignupRequest(name, email, password, confirmPassword)


    lifecycleScope.launch(Dispatchers.Main) {
        try {
            val result: ApiResponse<AuthResponse> = withContext(Dispatchers.IO) {

                signupRepository.signup(signupRequest)
            }

            when (result) {
                is ApiResponse.Success -> {
                    val data = result.data
                    Log.d("AUTH RESPONSE", "Success: $data")
                    val route =
                        EmailAuthScreenDirections.actionEmailAuthScreenToChatDest()
                    findNavController().navigate(route)
                }

                is ApiResponse.Error -> {
                    val errorMessage = result.message
                    Log.d("AUTH RESPONSE", "Failure: $errorMessage")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } catch (e: Exception) {
            // Handle any exceptions that may occur during the coroutine execution
            Log.d("AUTH RESPONSE", "Exception: $e")
            e.printStackTrace()
        }


    }
}*/
