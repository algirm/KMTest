package com.algirm.test2022.domain

import com.algirm.test2022.data.model.UserResponse
import com.algirm.test2022.util.Resource
import retrofit2.Response

interface UserRepository {
    suspend fun getUsers(pageNumber: Int, perPage: Int): Response<UserResponse>
}