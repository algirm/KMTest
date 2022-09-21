package com.algirm.test2022.data.remote

import com.algirm.test2022.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("api/users")
    suspend fun getUsers(
        @Query("page")
        pageNumber: Int = 1,
        @Query("per_page")
        perPage: Int = 10,
        @Query("delay")
        delay: Int = 3
    ): Response<UserResponse>

}