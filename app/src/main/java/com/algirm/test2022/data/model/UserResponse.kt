package com.algirm.test2022.data.model

data class UserResponse(
    val `data`: MutableList<User>,
    val page: Int,
    val per_page: Int,
    val support: Support,
    val total: Int,
    val total_pages: Int
)