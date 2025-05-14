package com.example.sparcs_new.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sparcs_new.network.NetworkSetModule
import com.example.sparcs_new.data.DataTokenStore

class AppViewModelFactory(
    private val context: Context,
    private val eventId: String? = null) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(
                NetworkSetModule.provideAuthApi(context),
                DataTokenStore(context)
            ) as T
        }
        else if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignupViewModel(
                NetworkSetModule.provideAuthApi(context)
            ) as T
        }
        else if (modelClass.isAssignableFrom(GetUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GetUserViewModel(
                NetworkSetModule.provideAuthApi(context)
            ) as T
        }
        else if (modelClass.isAssignableFrom(GetEventsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GetEventsViewModel(
                NetworkSetModule.provideAuthApi(context)
            ) as T
        }
        else if (modelClass.isAssignableFrom(GetAttendeesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GetAttendeesViewModel(
                NetworkSetModule.provideAuthApi(context),
                ) as T
        }
        else if (modelClass.isAssignableFrom(GetUserEventsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GetUserEventsViewModel(
                NetworkSetModule.provideAuthApi(context)
            ) as T
        }
        else if (modelClass.isAssignableFrom(UpdateNicknameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UpdateNicknameViewModel(
                NetworkSetModule.provideAuthApi(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}