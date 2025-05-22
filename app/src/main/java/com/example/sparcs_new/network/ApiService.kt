package com.example.sparcs_new.network

import com.example.sparcs_new.DTO.AttendeesResponseDTO
import com.example.sparcs_new.DTO.CommentResponseDTO
import com.example.sparcs_new.DTO.EventResponseDTO
import com.example.sparcs_new.DTO.LoginResponseDTO
import com.example.sparcs_new.DTO.RefreshTokenResponse
import com.example.sparcs_new.DTO.UserInfoResponseDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
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

    ): UserInfoResponseDTO

    @GET("user")
    suspend fun getUserId(
//        @Query("id") id: String
    ): UserInfoResponseDTO

    @PATCH("user")
    suspend fun updateNickname(
        @Query("nickname") nickname: String
    ):UserInfoResponseDTO

    //StartScreen
    @GET("events")
    suspend fun getEvents(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): List<EventResponseDTO>

    @POST("refresh-token")
    suspend fun refreshToken(
        @Query("refresh_token") refreshToken: String
    ): Response<RefreshTokenResponse>

    //attendee information
    @GET("events/{eventId}/attendees/")
    suspend fun getAttendees(
        @Path("eventId") eventId: String
    ): List<AttendeesResponseDTO>

    //이벤트 join, exit, info
    @POST("events/{eventId}/join/")
    suspend fun joinEvent(
        @Path("eventId") eventId: String
    ):String

    @POST("events/{eventId}/exit/")
    suspend fun exitEvent(
        @Path("eventId") eventId: String
    ):String

    @GET("events/joined")
    suspend fun getUserJoinedEvent(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): List<EventResponseDTO>

    @POST("events")
    suspend fun postEvent(
        @Query("title") title: String,
        @Query("datetime") datetime: String,
        @Query("location") location: String,
        @Query("description") description: String
    ): EventResponseDTO

    @PUT("events/{eventId}/")
    suspend fun editEvent(
        @Path("eventId") eventId: String,
        @Query("title") title: String,
        @Query("datetime") datetime: String,
        @Query("location") location: String,
        @Query("description") description: String
    ): EventResponseDTO

    @DELETE("events/{eventId}")
    suspend fun deleteEvent(
        @Path("eventId") eventId: String
    ): String

    //comment
    @POST("events/{eventId}/comments/")
    suspend fun addComment(
        @Path("eventId") eventId: String,
        @Query("content") content: String
    ): CommentResponseDTO

    @GET("events/{eventId}/comments")
    suspend fun getComment(
        @Path("eventId") eventId: String
    ):List<CommentResponseDTO>

}