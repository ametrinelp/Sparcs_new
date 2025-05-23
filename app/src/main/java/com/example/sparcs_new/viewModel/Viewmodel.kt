package com.example.sparcs_new.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparcs_new.DTO.CommentResponseDTO
import com.example.sparcs_new.DTO.EventResponseDTO
import com.example.sparcs_new.DTO.LoginResponseDTO
import com.example.sparcs_new.DTO.UserInfoResponseDTO
import com.example.sparcs_new.data.AuthManager
import com.example.sparcs_new.data.DataTokenStore
import com.example.sparcs_new.data.ThemePreference
import com.example.sparcs_new.network.AuthApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


class LoginViewModel(
    private val authApiService: AuthApiService,
    private val dataTokenStore: DataTokenStore
) : ViewModel()
{

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

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    val json = org.json.JSONObject(errorBody ?: "")
                    val detail = json.optString("detail")

                    when (detail) {
                        "Invalid credentials" -> "아이디 또는 비밀번호가 잘못되었습니다."
                        else -> detail.ifBlank { "로그인에 실패했습니다. (${e.code()})" }
                    }
                } catch (ex: Exception) {
                    "로그인 실패 (${e.code()})"
                }
                _loginState.value = LoginState.Error(errorMessage)

            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "알 수 없는 오류로 로그인 실패")
            }
        }
    }
    fun loginSuccess(){
        _loginState.value = LoginState.Idle}
}

class SignupViewModel(
    private val authApiService: AuthApiService) : ViewModel()
{

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun performSignup(username: String, password: String, nickname: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                 val response = authApiService.postupUser(username, password, nickname)
                _loginState.value = LoginState.Success(response)

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    val json = org.json.JSONObject(errorBody ?: "")
                    val detail = json.optString("detail")

                    when (detail) {
                        "Password must be at least 8 characters long, include at least one special character, and one number." -> "비밀번호는 8자 이상이어야 하며 특수문자와 숫자를 포함해야 합니다."
                        else -> detail.ifBlank { "회원 가입에 실패했습니다. (${e.code()})" }
                    }
                } catch (ex: Exception) {
                    "회원 가입 실패 (${e.code()})"
                }
                _loginState.value = LoginState.Error(errorMessage)

            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "알 수 없는 오류로 회원 가입 실패")
            }
        }
    }
}

class GetUserViewModel(
    private val authApiService: AuthApiService) : ViewModel()
{

    private val _getState = MutableStateFlow<GetState>(GetState.Idle)

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname.asStateFlow()

    private val _userid = MutableStateFlow("")
    val userid: StateFlow<String> = _userid.asStateFlow()

    private val _isUserInfoLoaded = MutableStateFlow(false)
    val isUserInfoLoaded: StateFlow<Boolean> = _isUserInfoLoaded.asStateFlow()

    fun getUserInfo() {
        viewModelScope.launch {
            _getState.value = GetState.Loading
            _isUserInfoLoaded.value = false
            try {
                val userInfoResponse = authApiService.getUser()
                val userIdInfoResponse = authApiService.getUserId()
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
) : ViewModel()
{
    private val _eventState = MutableStateFlow<GetEventState>(GetEventState.Idle)
    val eventState: StateFlow<GetEventState> = _eventState

    private val _offset = MutableStateFlow(0)
    val offset: StateFlow<Int> = _offset

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _isLoading = MutableStateFlow(false)

    fun goToNextPage() {
        _offset.value += 10
        _currentPage.value += 1
        getEventInfo(_offset.value)
    }

    fun goToPreviousPage() {
        if (_offset.value >= 10 && _currentPage.value > 1) {
            _offset.value -= 10
            _currentPage.value -= 1
            getEventInfo(_offset.value)
        }
    }
    fun getEventInfo(offset: Int) {
        if (_isLoading.value) return
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _offset.value = offset
                _eventState.value = GetEventState.Loading
                val events = authApiService.getEvents(offset, 10)

                _eventState.value = GetEventState.Success(events)
            } catch (e: Exception) {
                _eventState.value = GetEventState.Error(e.message ?: "이벤트 로딩 실패")
            }finally {
                _isLoading.value = false
            }
        }
    }
}

class GetAttendeesViewModel(
    private val authApiService: AuthApiService
) : ViewModel()
{

    private val _attendeeNicknames = MutableStateFlow<List<String>>(emptyList())
    val attendeeNicknames: StateFlow<List<String>> = _attendeeNicknames.asStateFlow()

    private val _attendeeEventId = MutableStateFlow<List<String>>(emptyList())
    val attendeeEventId: StateFlow<List<String>> = _attendeeEventId.asStateFlow()

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

                val responseid = authApiService.getAttendees(eventId)
                _attendeeEventId.value = responseid.map { eventId }

            } catch (e: Exception) {
                _error.value = "참가자를 불러오는 데 문제가 생겼어요. 오류 코드 : ${e.message}"
                _attendeeNicknames.value = listOf(_error.value) as List<String>
               } finally {
                _isLoading.value = false
            }
        }
    }

}

class JoinEventViewModel(
    private val authApiService: AuthApiService
) : ViewModel()
{
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun joinEvent(eventId:String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                authApiService.joinEvent(eventId)
            } catch (e: Exception) {
                _error.value = "error"
                } finally {
                _isLoading.value = false
            }
        }
    }
}

class ExitEventViewModel(
    private val authApiService: AuthApiService
) : ViewModel()
{
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun exitEvent(eventId:String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                authApiService.exitEvent(eventId)
            } catch (e: Exception) {
                _error.value = "error"
            } finally {
                _isLoading.value = false
            }
        }
    }

}

class GetUserJoinedEventsViewModel(
    private val authApiService: AuthApiService
) : ViewModel()
{
    private val _eventState = MutableStateFlow<GetEventState>(GetEventState.Idle)
    val eventState: StateFlow<GetEventState> = _eventState

    private val _offset = MutableStateFlow(0)
    val offset: StateFlow<Int> = _offset

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _isLoading = MutableStateFlow(false)

    fun goToNextPage() {
        _offset.value += 10
        _currentPage.value += 1
        getJoinedEventInfo(_offset.value)
    }

    fun goToPreviousPage() {
        if (_offset.value >= 10 && _currentPage.value > 1) {
            _offset.value -= 10
            _currentPage.value -= 1
            getJoinedEventInfo(_offset.value)
        }
    }

    fun getJoinedEventInfo(offset:Int) {
        viewModelScope.launch {
            _eventState.value = GetEventState.Loading
            try {
                _isLoading.value = true
                _offset.value = offset
                _eventState.value = GetEventState.Loading
                val events = authApiService.getUserJoinedEvent(offset, 10)
                _eventState.value = GetEventState.Success(events)
            } catch (e: Exception) {
                _eventState.value = GetEventState.Error(e.message ?: "이벤트 로딩 실패")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class UpdateNicknameViewModel(
    private val authApiService: AuthApiService
): ViewModel()
{
    private val _eventState = MutableStateFlow<GetState>(GetState.Idle)
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

class PostEventViewModel(
    private val authApiService: AuthApiService
) : ViewModel()
{
    private val _PostState = MutableStateFlow<PostEventState>(PostEventState.Idle)
    val postState: StateFlow<PostEventState> = _PostState

    fun postEvent(
        title : String,
        datetime : String,
        location : String,
        description : String) {

        viewModelScope.launch {
            _PostState.value = PostEventState.Loading
            try {
                val response = authApiService.postEvent(title, datetime, location, description)
                _PostState.value = PostEventState.Success(response)

            } catch (e: Exception) {
                _PostState.value = PostEventState.Error(e.message ?:"${e.message}")
            }
        }
    }
    fun resetPostStateToIdle() {
        _PostState.value = PostEventState.Idle
    }
}

class EditEventViewModel(
    private val authApiService: AuthApiService
) : ViewModel()
{

    private val _EventState = MutableStateFlow<PostEventState>(PostEventState.Idle)
    val eventState: StateFlow<PostEventState> = _EventState

    fun editEvent(
        eventId: String,
        title: String,
        description: String,
        location: String,
        datetime: String
    ) {

        viewModelScope.launch {
            _EventState.value = PostEventState.Loading
            try {

                val decodedTime = URLDecoder.decode(datetime, StandardCharsets.UTF_8.name())
                val response = authApiService.editEvent(eventId, title, decodedTime, location, description)
                _EventState.value = PostEventState.Success(response)
            } catch (e: Exception) {
                _EventState.value = PostEventState.Error(e.message ?:"${e.message}")
            }
        }
    }
}

class DeleteEventViewModel(
    private val authApiService: AuthApiService
) : ViewModel()
{
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun deleteEvent(eventId:String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                authApiService.deleteEvent(eventId)
            } catch (e: Exception) {
                _error.value = "error"
            } finally {
                _isLoading.value = false
            }
        }
    }

}

class ThemeViewModel(context: Context) : ViewModel() {
    private val themePreference = ThemePreference(context)

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    init {
        viewModelScope.launch {
            themePreference.isDarkThemeFlow.collect {
                _isDarkTheme.value = it
            }
        }
    }

    fun toggleTheme() {
        val newTheme = !_isDarkTheme.value
        _isDarkTheme.value = newTheme

        viewModelScope.launch {
            themePreference.saveTheme(newTheme)
        }
    }
}

class AddCommentViewModel(
    private val authApiService: AuthApiService
) : ViewModel()
{
    private val _eventState = MutableStateFlow<AddCommentState>(AddCommentState.Idle)
    val eventState: StateFlow<AddCommentState> = _eventState
    fun addCommentInfo(eventId: String, content: String) {
        viewModelScope.launch {
            _eventState.value = AddCommentState.Loading
            try {
                val events = authApiService.addComment(eventId, content)
                _eventState.value = AddCommentState.Success(events)
            } catch (e: Exception) {
                _eventState.value = AddCommentState.Error(e.message ?: "댓글 로딩 실패")
            }
        }
    }
}

class GetCommentViewModel(
    private val authApiService: AuthApiService
) : ViewModel()
{

    private val _emojiCount = MutableStateFlow<Map<String, Int>>(emptyMap())
    val emojiCount: StateFlow<Map<String, Int>> = _emojiCount.asStateFlow()

    private val _eventState = MutableStateFlow<GetCommentState>(GetCommentState.Idle)
    val eventState: StateFlow<GetCommentState> = _eventState

    fun getCommentInfo(eventId: String) {
        viewModelScope.launch {
            _eventState.value = GetCommentState.Loading
            try {
                val comments = authApiService.getComment(eventId)

                val counts = comments
                    .map { it.content }
                    .groupingBy { it }
                    .eachCount()

                _emojiCount.value = counts
                _eventState.value = GetCommentState.Success(comments)
            } catch (e: Exception) {
                _eventState.value = GetCommentState.Error(e.message ?: "댓글 로딩 실패")
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
sealed class PostEventState {
    data object Idle : PostEventState()
    data object Loading : PostEventState()
    data class Success(val response: EventResponseDTO) : PostEventState()
    data class Error(val message: String) : PostEventState()
}

sealed class AddCommentState {
    data object Idle : AddCommentState()
    data object Loading : AddCommentState()
    data class Success(val response: CommentResponseDTO) : AddCommentState()
    data class Error(val message: String) : AddCommentState()
}
sealed class GetCommentState {
    data object Idle : GetCommentState()
    data object Loading : GetCommentState()
    data class Success(val response: List<CommentResponseDTO>) : GetCommentState()
    data class Error(val message: String) : GetCommentState()
}