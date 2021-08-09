package com.jhs.wiken.vo

import com.jhs.wiken.util.Ut

// Ken 소스 해석기
class KenSourceInterpreter(
    val hasConfig: Boolean,
    private val source: String,
    private val kenConfig: KenConfig
) {
    companion object {
        fun from(source: String): KenSourceInterpreter {
            // 문서에 config이 들어있는지 확인
            val hasConfig = source.contains("$$" + "config")

            val sourceConfig = if (hasConfig) {
                val configBits = source.split("$$", limit = 3)
                configBits[1].substring(6).trim()
            } else {
                ""
            }

            val kenConfig = KenConfig.from(sourceConfig)
            return KenSourceInterpreter(kenConfig.isExists, source, kenConfig)
        }
    }

    fun getTitle(): String {
        return kenConfig.title
    }

    fun getKenConfigSource(): String {
        return kenConfig.source
    }
}
