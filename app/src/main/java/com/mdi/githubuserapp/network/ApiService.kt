package com.mdi.githubuserapp.network

import com.mdi.githubuserapp.response.FollowResponse
import com.mdi.githubuserapp.response.SearchUserResponse
import com.mdi.githubuserapp.response.UserDetailResponse
import com.mdi.githubuserapp.response.UsersResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getUsers(
        @Header("Authorization") token: String = "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"
    ): Call<List<UsersResponse>>

    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String,
        @Header("Authorization") token: String = "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"
    ): Call<UserDetailResponse>

    @GET("search/users?")
    fun getSearchUser(
        @Query("q") username: String,
        @Header("Authorization") token: String = "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"
    ): Call<SearchUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String,
        @Header("Authorization") token: String = "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"
    ): Call<List<FollowResponse>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String,
        @Header("Authorization") token: String = "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"
    ): Call<List<FollowResponse>>
}