package com.panther.contentai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.panther.contentai.arch_comp.CuratorViewModel
import com.panther.contentai.databinding.FragmentLogInBinding
import com.panther.contentai.util.Resource
import com.panther.contentai.util.isValid
import kotlinx.coroutines.launch


class LogIn : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val curatorViewModel by activityViewModels<CuratorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signInBtn.setOnClickListener {
            val email = binding.editTextLoginEmail.text.toString()
            val password = binding.editTextLoginPassword.text.toString()

            if (email.trim().isEmpty() || password.trim().isEmpty()){
                Toast.makeText(requireContext(), "Field cannot be empty", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            curatorViewModel.signInUser(requireContext(),email, password)
            observeSignInState()
        }
    }

    private fun observeSignInState() {
        lifecycleScope.launch {
            curatorViewModel.userDataState.collect { state ->
                binding.apply {
                    when (state) {

                        is Resource.Loading -> {
                            progressBar.isVisible = true
                        }

                        is Resource.Successful -> {
                            progressBar.isVisible = false
                            try {
                                val route =
                                    LogInDirections.actionLogInToChatDest()
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