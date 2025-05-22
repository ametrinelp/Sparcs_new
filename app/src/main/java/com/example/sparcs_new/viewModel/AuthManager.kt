package com.example.sparcs_new.viewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AuthManager {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun setAuthenticated() {
        _authState.value = AuthState.Authenticated
    }

    fun setUnauthenticated() {
        _authState.value = AuthState.Unauthenticated
    }

    fun setLoading() {
        _authState.value = AuthState.Loading
    }
}
sealed class AuthState {
    data object Authenticated : AuthState()
    data object Unauthenticated : AuthState()
    data object Loading : AuthState()
}
