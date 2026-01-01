package com.example.careercounselingchatbot.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.example.careercounselingchatbot.data.model.User
import com.example.careercounselingchatbot.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        val currentUser = repository.getCurrentUserData()
        if (currentUser != null) {
            _user.value = currentUser
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun signUp(email: String, password: String, displayName: String) {
        if (!validateSignUpInput(email, password, displayName)) return

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            val result = repository.signUpWithEmail(email, password, displayName)

            result.onSuccess { user ->
                _user.value = user
                _authState.value = AuthState.Authenticated
            }.onFailure { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Sign up failed")
            }
        }
    }

    fun signIn(email: String, password: String) {
        if (!validateSignInInput(email, password)) return

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            val result = repository.signInWithEmail(email, password)

            result.onSuccess { user ->
                _user.value = user
                _authState.value = AuthState.Authenticated
            }.onFailure { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Sign in failed")
            }
        }
    }

    fun signInWithGoogle(account: GoogleSignInAccount) {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            val result = repository.signInWithGoogle(account)

            result.onSuccess { user ->
                _user.value = user
                _authState.value = AuthState.Authenticated
            }.onFailure { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Google sign in failed")
            }
        }
    }

    fun signOut() {
        repository.signOut()
        _user.value = null
        _authState.value = AuthState.Unauthenticated
    }

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _authState.value = AuthState.Error("Please enter your email")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            val result = repository.sendPasswordResetEmail(email)

            result.onSuccess {
                _authState.value = AuthState.PasswordResetSent
            }.onFailure { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Failed to send reset email")
            }
        }
    }

    private fun validateSignUpInput(email: String, password: String, displayName: String): Boolean {
        when {
            displayName.isBlank() -> {
                _authState.value = AuthState.Error("Please enter your name")
                return false
            }
            email.isBlank() -> {
                _authState.value = AuthState.Error("Please enter your email")
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _authState.value = AuthState.Error("Please enter a valid email")
                return false
            }
            password.length < 6 -> {
                _authState.value = AuthState.Error("Password must be at least 6 characters")
                return false
            }
        }
        return true
    }

    private fun validateSignInInput(email: String, password: String): Boolean {
        when {
            email.isBlank() -> {
                _authState.value = AuthState.Error("Please enter your email")
                return false
            }
            password.isBlank() -> {
                _authState.value = AuthState.Error("Please enter your password")
                return false
            }
        }
        return true
    }

    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Unauthenticated
        }
    }

    sealed class AuthState {
        object Unauthenticated : AuthState()
        object Loading : AuthState()
        object Authenticated : AuthState()
        object PasswordResetSent : AuthState()
        data class Error(val message: String) : AuthState()
    }
}