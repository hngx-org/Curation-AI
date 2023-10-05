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

                //Getting the user cookie/Id
                val sessionCookie = "ession=96227082-c61d-4f12-90d8-dd9fc4a7a11b.meKUKXc5b3UoWQZ8kqja2giM5Zc; Expires=Sat, 04 Nov 2023 20:07:28 GMT; Path=/;"
                val cookie: String = "session=96227082-c61d-4f12-90d8-dd9fc4a7a11b.meKUKXc5b3UoWQZ8kqja2giM5Zc"

                val test = sessionCookie.substring(0, sessionCookie.indexOf(";"))
                Log.d("sessionCookie", "Cookie $test")

               //cookie = RetrofitClient.getCookiesForUrl().toString()
                Log.d("cookie", "Cookie generated: $cookie")

                //Instantiate OpenAI
                val openApiClient = OpenAiCaller
                // Generate a response based on the user's input
                val response =
                    openApiClient.generateChatResponse(messageTxt.toString(), cookie)
                Log.i("Test", "The response is $response")

                // Update the UI on the main thread
                withContext(Dispatchers.Main) {
                    val userChatMessage =
                        Chat(cookie, null, messageTxt.toString(), "User")
                    chatViewModel.addMessage(userChatMessage)
                    messageTxt?.clear()

                    val aiChatMessage =
                        Chat(cookie, response, null, "AI")
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