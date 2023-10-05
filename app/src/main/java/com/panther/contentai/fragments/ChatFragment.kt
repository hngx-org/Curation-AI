package com.panther.contentai.fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.panther.contentai.R
import com.panther.contentai.arch_comp.CuratorViewModel
import com.panther.contentai.databinding.FragmentChatBinding
import com.panther.contentai.util.Resource
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private var exitAppToastStillShowing = false
    private val curatorViewModel by activityViewModels<CuratorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        showMenu(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            isEnabled = true
            exitApp()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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

    private fun showMenu(savedInstanceState: Bundle?) {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.new_chat_dest -> {
                        curatorViewModel.logOut()
                        observeSignOutState(savedInstanceState)
                        true
                    }

                    else -> false
                }

            }
        }, viewLifecycleOwner, Lifecycle.State.STARTED)
    }

    private fun observeSignOutState(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            curatorViewModel.logOutState.collect { state ->
                when (state) {

                    is Resource.Loading -> {
                        Toast.makeText(requireContext(), "Signing out", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Resource.Successful -> {
                        try {
                            val route =
                                ChatFragmentDirections.actionChatDestToSigninScreen()
                            findNavController().navigate(route)
                        }catch (e:Exception){
                            val navOptions = NavOptions.Builder()
                                .setPopUpTo(R.id.emailAuthScreen, true)
                                .build()
                            findNavController().navigate(R.id.signinScreen,savedInstanceState,navOptions)
                        }

                        Toast.makeText(requireContext(), "Sign out successful", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Resource.Failure -> {
                        Toast.makeText(requireContext(), state.msg, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }


}