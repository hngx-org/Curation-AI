package com.panther.contentai.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apilibrary.wrapperclass.OpenAiCaller
import com.panther.contentai.viewmodel.ChatViewModel
import com.panther.contentai.R
import com.panther.contentai.adapter.AiChatAdapter
import com.panther.contentai.databinding.FragmentChatBinding
import com.panther.contentai.models.Chat
import com.shegs.hng_auth_library.authlibrary.AuthLibrary
import com.shegs.hng_auth_library.model.AuthResponse
import com.shegs.hng_auth_library.network.ApiResponse
import com.shegs.hng_auth_library.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ChatFragment : Fragment(), MenuProvider {


    private lateinit var chatadapter: AiChatAdapter
    private lateinit var chatViewModel: ChatViewModel
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private var exitAppToastStillShowing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            isEnabled = true
            exitApp()
        }
        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        setupRecyclerView()


        binding.sendBtnImageView.setOnClickListener {
            val messageTxt = binding.messageEt.text

            CoroutineScope(Dispatchers.IO).launch {

                var cookie: String = ""
                cookie = RetrofitClient.getCookiesForUrl().toString()

                val sessionCookie = "session=f63db3fd-9f33-4ad0-85c8-4588d1fc473d.cCkHyhUMDB2ww5E9EzSUIMofiPA"

                //Extracting sessionCookie from the cookie
                //val sessionCookie = cookie.substring(0, cookie.indexOf(";"))
                Log.d("Cookie", "Cookie generated: $sessionCookie")

                //Instantiate OpenAI
                val openApiClient = OpenAiCaller
                // Generate a response based on the user's input
                val response =
                    openApiClient.generateChatResponse(messageTxt.toString(), sessionCookie)
                Log.i("Test", "The response is $response")

                // Update the UI on the main thread
                withContext(Dispatchers.Main) {
                    val userChatMessage =
                        Chat(sessionCookie, null, null, messageTxt.toString().trim())
                    chatViewModel.addMessage(userChatMessage)
                    messageTxt?.clear()

                    val aiChatMessage =
                        Chat(sessionCookie, null, null, response)
                    chatViewModel.addMessage(aiChatMessage)
                }

            }
        }


    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.new_chat_dest -> {

            }
        }
        return true
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

    private fun setupRecyclerView() {

        chatadapter = AiChatAdapter()

        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            //layoutManager.reverseLayout = true
            adapter = chatadapter

            chatViewModel.chatMessages.observe(viewLifecycleOwner) { receivedMessage ->

                chatadapter.differ.submitList(receivedMessage)
                // This scrolls to the latest message
                //smoothScrollToPosition(receivedMessage.size -1)
            }
        }
    }


}