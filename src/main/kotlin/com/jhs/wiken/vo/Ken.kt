package com.jhs.wiken.vo

data class Ken(
    val id: Int,
    val regDate: String,
    val updateDate: String,
    val memberId: Int,
    val title: String,
    val source: String,
    val result: String,
) {
    fun genKenConfigSource(): String {
        val kenSourceInterpreter = KenSourceInterpreter.from(source)

        return kenSourceInterpreter.getKenConfigSource()
    }

    fun getKenConfig(): KenConfig {
        return genSourceInterpreter().kenConfig
    }

    fun genSourceInterpreter(): KenSourceInterpreter {
        return KenSourceInterpreter.from(source)
    }
}
