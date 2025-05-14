package com.example.sparcs_new.DTO

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime


data class EventResponseDTO(
    @SerializedName("title") val title : String = "",
    @SerializedName("description") val description : String = "",
    @SerializedName("id") val event_id : String = "",
    @SerializedName("location") val location : String = "",
    @SerializedName("datetime") val datetime : String = "",
    @SerializedName("user_id") val user_id : String = "",
    @SerializedName("created_at") val createdTime : String = "",
    @SerializedName("updated_at") val updatedTime : String? = "",
)

data class AttendeesResponseDTO(
    @SerializedName("id") val id: String,
    @SerializedName("password") val password: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("username") val username: String
)

data class GetUserEventsDTO(
    @SerializedName("user_id") val user_id: String = "",
    @SerializedName("offset") val offset : Int = 0,
    @SerializedName("limit") val limit : Int = 10
)