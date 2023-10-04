package com.panther.contentai.arch_comp

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panther.contentai.util.Resource
import com.shegs.hng_auth_library.authlibrary.AuthLibrary
import com.shegs.hng_auth_library.model.LoginRequest
import com.shegs.hng_auth_library.model.SignupRequest
import com.shegs.hng_auth_library.model.UserData
import com.shegs.hng_auth_library.network.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CuratorViewModel : ViewModel() {

//    private val apiService = AuthLibrary.createAuthService()
//    private val signupRepository = AuthLibrary.createSignupRepository(apiService)

    var userDataState = MutableStateFlow<Resource<UserData>>(Resource.Loading())
        private set

    fun signUpUser(name: String, email: String, password: String, confirmPassword: String) {
        userDataState.value = Resource.Loading()
        val apiService = AuthLibrary.createAuthService()
        val signupRepository = AuthLibrary.createSignupRepository(apiService)
        val signupRequest = SignupRequest(name, email, password, confirmPassword)


        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = signupRepository.signup(signupRequest)

                when (result) {
                    is ApiResponse.Success -> {
                        val data = result.data
                        Log.d("AUTH RESPONSE", "SignUp-Success: $data")
                        userDataState.value = Resource.Successful(result.data.data)
                    }

                    is ApiResponse.Error -> {
                        val errorMessage = result.message
                        Log.d("AUTH RESPONSE", "SignUp-Failure: $errorMessage")
                        userDataState.value = Resource.Failure(errorMessage)
                    }
                }
            } catch (e: Exception) {
                // Handle any exceptions that may occur during the coroutine execution
                Log.d("AUTH RESPONSE", "Exception: $e")
                userDataState.value = Resource.Failure(e.message)
                e.printStackTrace()
            }


        }
    }

    fun signInUser(context: Context,email: String, password: String) {
        userDataState.value = Resource.Loading()
        val apiService = AuthLibrary.createAuthService()
        val dataStoreRepository = AuthLibrary.createDataStoreRepository(context)
        val signInRepository = AuthLibrary.createLoginRepository(apiService,dataStoreRepository)
        val signInRequest = LoginRequest(email, password)


        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = signInRepository.login(signInRequest)

                when (result) {
                    is ApiResponse.Success -> {
                        val data = result.data
                        Log.d("AUTH RESPONSE", "SignIn-Success: $data")
                        userDataState.value = Resource.Successful(result.data.data)
                    }

                    is ApiResponse.Error -> {
                        val errorMessage = result.message
                        Log.d("AUTH RESPONSE", "SignIn-Failure: $errorMessage")
                        userDataState.value = Resource.Failure(errorMessage)
                    }
                }
            } catch (e: Exception) {
                // Handle any exceptions that may occur during the coroutine execution
                Log.d("AUTH RESPONSE", "Exception: $e")
                userDataState.value = Resource.Failure(e.message)
                e.printStackTrace()
            }


        }
    }
}