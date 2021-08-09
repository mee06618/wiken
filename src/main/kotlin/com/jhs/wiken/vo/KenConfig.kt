package com.jhs.wiken.vo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.jhs.wiken.util.Ut

@JsonIgnoreProperties(ignoreUnknown = true)
data class KenConfig(
    val title: String,
    val keywords: List<String>
) {
    companion object {
        fun from(configSource: String): KenConfig {
            if ( configSource.isEmpty() ) {
                return fromEmpty()
            }

            return Ut.getObjFromYmlStr<KenConfig>(configSource)
        }

        fun fromEmpty(): KenConfig {
            return KenConfig("", arrayListOf())
        }
    }
}
