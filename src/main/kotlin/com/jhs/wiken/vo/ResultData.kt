package com.jhs.wiken.vo

class ResultData<T>(
    val resultCode: String,
    val msg: String,
    private val data1Name: String,
    private val data1: T
) {
    companion object {
        fun from(resultCode: String, msg: String): ResultData<String> {
            return ResultData(resultCode, msg, "", "")
        }

        fun <T> from(resultCode: String, msg: String, data1Name: String, data1: T): ResultData<T> {
            return ResultData(resultCode, msg, data1Name, data1)
        }
    }

    val isSuccess
        get() = resultCode.startsWith("S-")

    val isFail
        get() = !isSuccess

    val data: T
        get() = data1
}
