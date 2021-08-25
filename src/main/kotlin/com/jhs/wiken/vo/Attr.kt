package com.jhs.wiken.vo

data class Attr(
    val id: Int,
    val regDate: String,
    val updateDate: String,
    val relTypeCode: String,
    val relId: Int,
    val typeCode: String,
    val type2Code: String,
    val value: String?,
    val expireDate: String?, 
)