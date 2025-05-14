package com.example.sparcs_new.network

import com.example.sparcs_new.DTO.AttendeesResponseDTO
import com.example.sparcs_new.DTO.EventResponseDTO
import com.example.sparcs_new.DTO.GetUserEventsDTO
import com.example.sparcs_new.DTO.LoginResponseDTO
import com.example.sparcs_new.DTO.RefreshTokenRequest
import com.example.sparcs_new.DTO.RefreshTokenResponse
import com.example.sparcs_new.DTO.UserInfoResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path
interface AuthApiService {
    //로그인
    @POST("signin")
    suspend fun postinUser(
    @Query("username") username: String,
    @Query("password") password: String
    ): LoginResponseDTO

    //회원가입
    @POST("signup")
    suspend fun postupUser(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("nickname") nickname: String,
    ): LoginResponseDTO

    //유저 정보
    @GET("user")
    suspend fun getUser(
        @Query("username") username: String,
        @Query("nickname") nickname: String
    ): UserInfoResponseDTO

    @GET("user")
    suspend fun getUserId(
        @Query("id") id: String
    ): UserInfoResponseDTO

    //StartScreen
    @GET("events")
    suspend fun getEvents(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): List<EventResponseDTO>

    @POST("refresh-token")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<RefreshTokenResponse>

    //attendee information
    @GET("events/{eventId}/attendees/")
    suspend fun getAttendees(
        @Path("eventId") eventId: String
    ): List<AttendeesResponseDTO>

    @GET("users/{user_id}/events")
    suspend fun getUserEvents(
        @Path("user_id") user_id: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): List<EventResponseDTO>

    @PATCH("user")
    suspend fun updateNickname(
        @Query("nickname") nickname: String
    ):UserInfoResponseDTO
}