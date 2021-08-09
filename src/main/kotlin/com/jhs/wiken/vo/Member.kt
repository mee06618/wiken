package com.jhs.wiken.vo

data class Member(
    val id: Int,
    val regDate: String,
    val updateDate: String,
    val loginId: String,
    val loginPw: String,
    val name: String,
    val nickname: String,
    val cellphoneNo: String,
    val email: String,
)