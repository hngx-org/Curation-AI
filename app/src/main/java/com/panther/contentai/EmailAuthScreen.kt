package com.panther.contentai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.content.Context

import androidx.navigation.fragment.findNavController
import com.panther.contentai.databinding.FragmentEmailAuthScreenBinding
import com.shegs.hng_auth_library.authlibrary.AuthLibrary
import com.shegs.hng_auth_library.model.AuthResponse
import com.shegs.hng_auth_library.model.SignupRequest
import com.shegs.hng_auth_library.network.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
        val apiService = AuthLibrary.createAuthService()
        val dataStoreRepository = AuthLibrary.createDataStoreRepository(requireContext())
        val signupRepository = AuthLibrary.createSignupRepository(apiService)

        val signupRequest = SignupRequest(
            name = "John Doe",
            email = "johndoe@example.com",
            password = "password123",
            confirm_password = "password123"
        )


        emailBinding.signUpBtn.setOnClickListener {

            val name = emailBinding.editTextName.text.toString()
            val email = emailBinding.editTextEmail.text.toString()
            val password = emailBinding.editTextEmail.text.toString()
            val confirmPassword = emailBinding.editTextConfirmPassword.text.toString()

            val signupRequest = SignupRequest(name, email, password, confirmPassword)


                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val result: ApiResponse<AuthResponse> = withContext(Dispatchers.IO) {

                            signupRepository.signup(signupRequest)
                        }

                        when (result) {
                            is ApiResponse.Success -> {
                                val data = result.data
                                val route =
                                    EmailAuthScreenDirections.actionEmailAuthScreenToChatDest()
                                findNavController().navigate(route)
                            }

                            is ApiResponse.Error -> {
                                val errorMessage = result.message
                                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } catch (e: Exception) {
                        // Handle any exceptions that may occur during the coroutine execution
                        e.printStackTrace()
                    }


                }
            }
        }
    }

