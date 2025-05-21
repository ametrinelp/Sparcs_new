package com.example.sparcs_new.DTO

import com.google.gson.annotations.SerializedName


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

data class CommentResponseDTO(
    @SerializedName("user_id") val user_id: String,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("event_id") val event_id: String,
    @SerializedName("id") val id: String
)