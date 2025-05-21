package com.example.sparcs_new.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sparcs_new.network.NetworkSetModule
import com.example.sparcs_new.data.DataTokenStore

class AppViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val authApi = NetworkSetModule.provideAuthApi(context)
        val dataTokenStore = DataTokenStore(context)

        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authApi, dataTokenStore) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(authApi) as T
            }
            modelClass.isAssignableFrom(GetUserViewModel::class.java) -> {
                GetUserViewModel(authApi) as T
            }
            modelClass.isAssignableFrom(GetUserEventsViewModel::class.java) -> {
                GetUserEventsViewModel(authApi) as T
            }
            modelClass.isAssignableFrom(GetEventsViewModel::class.java) -> {
                GetEventsViewModel(authApi) as T
            }
            modelClass.isAssignableFrom(GetAttendeesViewModel::class.java) -> {
                GetAttendeesViewModel(authApi) as T
            }
            modelClass.isAssignableFrom(UpdateNicknameViewModel::class.java) -> {
                UpdateNicknameViewModel(authApi) as T
            }
            modelClass.isAssignableFrom(JoinEventViewModel::class.java) -> {
                JoinEventViewModel(authApi) as T
            }
            modelClass.isAssignableFrom(ExitEventViewModel::class.java) -> {
                ExitEventViewModel(authApi) as T
            }
            modelClass.isAssignableFrom(PostEventViewModel::class.java) -> {
                PostEventViewModel(authApi) as T
            }
            modelClass.isAssignableFrom(GetUserJoinedEventsViewModel::class.java) -> {
                GetUserJoinedEventsViewModel(authApi) as T
            }
            modelClass.isAssignableFrom(EditEventViewModel::class.java) -> {
                EditEventViewModel(authApi) as T
            }
            modelClass.isAssignableFrom(DeleteEventViewModel::class.java) -> {
                DeleteEventViewModel(authApi) as T
            }
            modelClass.isAssignableFrom(GetCommentViewModel::class.java) -> {
                GetCommentViewModel(authApi) as T
            }
            modelClass.isAssignableFrom(AddCommentViewModel::class.java) -> {
                AddCommentViewModel(authApi) as T
            }
            modelClass.isAssignableFrom(ThemeViewModel::class.java) -> {
                ThemeViewModel(context) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}