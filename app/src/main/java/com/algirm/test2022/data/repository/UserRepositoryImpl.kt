package com.algirm.test2022.data.repository

import com.algirm.test2022.data.remote.UserApi
import com.algirm.test2022.domain.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {

    override suspend fun getUsers(pageNumber: Int, perPage: Int) =
        userApi.getUsers(pageNumber, perPage)

}