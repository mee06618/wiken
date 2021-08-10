package com.jhs.wiken.vo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.jhs.wiken.util.Ut

@JsonIgnoreProperties(ignoreUnknown = true)
data class KenConfig(
    val isExists: Boolean = true,
    val title: String,
    val keywords: List<String>,
    val typeCode: String = "doc", // doc, blog, series
    val articles: List<Int> = arrayListOf()
) {
    var source:String = ""

    companion object {
        fun from(configSource: String): KenConfig {
            if ( configSource.isEmpty() ) {
                return fromEmpty()
            }

            val kenConfig = Ut.getObjFromYmlStr<KenConfig>(configSource)
            kenConfig.source = configSource

            return kenConfig
        }

        fun fromEmpty(): KenConfig {
            return KenConfig(false, "", arrayListOf())
        }
    }
}
