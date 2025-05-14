package com.example.sparcs_new.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparcs_new.DTO.AttendeesResponseDTO
import com.example.sparcs_new.DTO.EventResponseDTO
import com.example.sparcs_new.DTO.GetUserEventsDTO
import com.example.sparcs_new.DTO.LoginResponseDTO
import com.example.sparcs_new.DTO.UserInfoResponseDTO
import com.example.sparcs_new.data.DataTokenStore
import com.example.sparcs_new.network.AuthApiService
import com.google.firebase.appdistribution.gradle.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LoginViewModel(
    private val authApiService: AuthApiService,
    private val dataTokenStore: DataTokenStore,
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun performLogin(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = authApiService.postinUser(username, password)

                dataTokenStore.saveAccessToken(response.access_token)
                dataTokenStore.saveRefreshToken(response.refresh_token)


                AuthManager.setAuthenticated()
                _loginState.value = LoginState.Success(response)
            } catch (e: Exception) {
                AuthManager.setUnauthenticated()
                _loginState.value = LoginState.Error(e.message ?: "로그인 실패")
            }
        }
    }

    fun loginSuccess(){
        _loginState.value = LoginState.Idle}
}

class SignupViewModel(
    private val authApiService: AuthApiService) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun performSignup(username: String, password: String, nickname: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                 val response = authApiService.postupUser(username, password, nickname)
                _loginState.value = LoginState.Success(response)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "error")
            }
        }
    }
}

class GetUserViewModel(
    private val authApiService: AuthApiService) : ViewModel() {

    private val _getState = MutableStateFlow<GetState>(GetState.Idle)
    val getState: StateFlow<GetState> = _getState

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname.asStateFlow()

    private val _userid = MutableStateFlow("")
    val userid: StateFlow<String> = _userid.asStateFlow()

    private val _isUserInfoLoaded = MutableStateFlow(false)
    val isUserInfoLoaded: StateFlow<Boolean> = _isUserInfoLoaded.asStateFlow()

    fun GetUserInfo(userQuery: String, nickQuery: String) {
        viewModelScope.launch {
            _getState.value = GetState.Loading
            _isUserInfoLoaded.value = false
            try {
                val userInfoResponse = authApiService.getUser(userQuery, nickQuery)
                val userIdInfoResponse = authApiService.getUserId(userQuery)
                _username.value = userInfoResponse.username
                _nickname.value = userInfoResponse.nickname
                _userid.value = userIdInfoResponse.id

                _getState.value = GetState.Success(userInfoResponse)
                _isUserInfoLoaded.value = true
            } catch (e: Exception) {
                _getState.value = GetState.Error(e.message ?: "error")
                _isUserInfoLoaded.value = true
                _username.value = ""
                _nickname.value = ""
                _userid.value = ""

            }
        }
    }
}


class GetEventsViewModel(
    private val authApiService: AuthApiService
) : ViewModel() {
    private val _eventState = MutableStateFlow<GetEventState>(GetEventState.Idle)
    val eventState: StateFlow<GetEventState> = _eventState
    fun getEventInfo() {
        viewModelScope.launch {
            _eventState.value = GetEventState.Loading
            try {
                val events = authApiService.getEvents(0, 10)
                _eventState.value = GetEventState.Success(events)
            } catch (e: Exception) {
                _eventState.value = GetEventState.Error(e.message ?: "이벤트 로딩 실패")
            }
        }
    }
}

class GetAttendeesViewModel(
    private val authApiService: AuthApiService
) : ViewModel() {

    private val _attendeeNicknames = MutableStateFlow<List<String>>(emptyList())
    val attendeeNicknames: StateFlow<List<String>> = _attendeeNicknames.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()


    fun loadAttendees(eventId:String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = authApiService.getAttendees(eventId)
                _attendeeNicknames.value = response.map { it.nickname }
            } catch (e: Exception) {
                _error.value = "참가자를 불러오는 데 문제가 생겼어요. 오류 코드 : ${e.message}"
                _attendeeNicknames.value = listOf(_error.value) as List<String>
               } finally {
                _isLoading.value = false
            }
        }
    }

}

class GetUserEventsViewModel(
    private val authApiService: AuthApiService
) : ViewModel() {
    private val _eventState = MutableStateFlow<GetEventState>(GetEventState.Idle)
    val eventState: StateFlow<GetEventState> = _eventState
    fun getUserEventInfo(user_id : String) {
        viewModelScope.launch {
            _eventState.value = GetEventState.Loading
            try {
                val events = authApiService.getUserEvents(user_id ,0, 10)
                _eventState.value = GetEventState.Success(events)
            } catch (e: Exception) {
                _eventState.value = GetEventState.Error(e.message ?: "나의 이벤트 로딩 실패")

            }
        }
    }
}

class UpdateNicknameViewModel(
    private val authApiService: AuthApiService
): ViewModel(){
    private val _eventState = MutableStateFlow<GetState>(GetState.Idle)
    val eventState: StateFlow<GetState> = _eventState
    fun updateUserNickname(nickname : String) {
        viewModelScope.launch {
            _eventState.value = GetState.Loading
            try {
                val events = authApiService.updateNickname(nickname)
                _eventState.value = GetState.Success(events)
            } catch (e: Exception) {
                _eventState.value = GetState.Error(e.message ?: "나의 이벤트 로딩 실패")

            }
        }
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val response: LoginResponseDTO) : LoginState()
    data class Error(val message: String) : LoginState()
}
sealed class GetState {
    data object Idle : GetState()
    data object Loading : GetState()
    data class Success(val response: UserInfoResponseDTO) : GetState()
    data class Error(val message: String) : GetState()
}
sealed class GetEventState {
    data object Idle : GetEventState()
    data object Loading : GetEventState()
    data class Success(val response: List<EventResponseDTO>) : GetEventState()
    data class Error(val message: String) : GetEventState()
}
