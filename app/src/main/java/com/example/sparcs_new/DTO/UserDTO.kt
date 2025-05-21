package com.example.sparcs_new.DTO

import com.google.gson.annotations.SerializedName

data class LoginResponseDTO(
    @SerializedName("access_token") val access_token: String,
    @SerializedName("refresh_token") val refresh_token: String
)
data class UserInfoResponseDTO(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("nickname") val nickname: String
)

data class RefreshTokenRequest(
    @SerializedName("refresh_token") val refreshToken: String
)

data class RefreshTokenResponse(
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("access_token") val accessToken: String
)

