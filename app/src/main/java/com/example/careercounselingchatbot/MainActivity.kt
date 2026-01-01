package com.example.careercounselingchatbot.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.careercounselingchatbot.R
import com.example.careercounselingchatbot.data.repository.AuthRepository
import com.example.careercounselingchatbot.databinding.ActivityMainBinding
import com.example.careercounselingchatbot.ui.adapter.ChatAdapter
import com.example.careercounselingchatbot.ui.auth.LoginActivity
import com.example.careercounselingchatbot.ui.viewmodel.AuthViewModel
import com.example.careercounselingchatbot.ui.viewmodel.ChatViewModel
import com.example.careercounselingchatbot.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val chatViewModel: ChatViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory(AuthRepository())
    }
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is authenticated
        checkAuthentication()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupInputField()
        setupObservers()
        displayUserInfo()
    }

    private fun checkAuthentication() {
        if (AuthRepository().currentUser == null) {
            navigateToLogin()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.recyclerViewChat.apply {
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
        }
    }

    private fun setupInputField() {
        binding.buttonSend.setOnClickListener {
            sendMessage()
        }

        binding.editTextMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else {
                false
            }
        }
    }

    private fun sendMessage() {
        val message = binding.editTextMessage.text.toString()
        if (message.isNotBlank()) {
            chatViewModel.sendMessage(message)
            binding.editTextMessage.text?.clear()
            binding.editTextMessage.clearFocus()
        }
    }

    private fun setupObservers() {
        chatViewModel.messages.observe(this) { messages ->
            chatAdapter.updateMessages(messages)
            binding.recyclerViewChat.smoothScrollToPosition(messages.size - 1)
        }

        chatViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.buttonSend.isEnabled = !isLoading
        }

        chatViewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                chatViewModel.clearError()
            }
        }

        authViewModel.user.observe(this) { user ->
            user?.let {
                binding.toolbar.subtitle = "Welcome, ${it.displayName}"
            }
        }
    }

    private fun displayUserInfo() {
        authViewModel.user.value?.let { user ->
            binding.toolbar.subtitle = "Welcome, ${user.displayName}"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear -> {
                showClearChatDialog()
                true
            }
            R.id.action_sign_out -> {
                showSignOutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showClearChatDialog() {
        AlertDialog.Builder(this)
            .setTitle("Clear Chat")
            .setMessage("Are you sure you want to clear the entire conversation?")
            .setPositiveButton("Clear") { _, _ ->
                chatViewModel.clearChat()
                Toast.makeText(this, "Chat cleared", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showSignOutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Sign Out")
            .setMessage("Are you sure you want to sign out?")
            .setPositiveButton("Sign Out") { _, _ ->
                authViewModel.signOut()
                navigateToLogin()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}