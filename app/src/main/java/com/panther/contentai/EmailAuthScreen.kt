package com.panther.contentai

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.panther.contentai.arch_comp.CuratorViewModel
import com.panther.contentai.databinding.FragmentEmailAuthScreenBinding
import com.panther.contentai.util.Resource
import com.panther.contentai.util.isValid
import kotlinx.coroutines.launch


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
                            try {
                                val route =
                                    EmailAuthScreenDirections.actionEmailAuthScreenToChatDest()
                                findNavController().navigate(route)
                            }catch (e:Exception){
                                findNavController().navigate(R.id.chat_dest)
                            }

                        }

                        is Resource.Failure -> {
                            progressBar.isVisible = false
                            Toast.makeText(requireContext(), state.msg.isValid("Oops.. Unable to sign up"), Toast.LENGTH_SHORT)
                                .show()

                        }
                    }
                }
            }
        }
    }
}