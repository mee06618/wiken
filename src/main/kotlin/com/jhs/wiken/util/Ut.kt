package com.jhs.wiken.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.net.URLEncoder


class Ut {
    companion object {
        inline fun <reified T> getObjFromJsonStr(jsonStr: String): T {
            val mapper = ObjectMapper().registerKotlinModule()

            return mapper.readValue<T>(jsonStr)
        }

        fun getJsonStrFromObj(obj: Any): String {
            val mapper = ObjectMapper().registerKotlinModule()

            return mapper.writeValueAsString(obj)
        }

        inline fun <reified T> getObjFromYmlStr(ymlStr: String): T {
            val mapper = ObjectMapper(YAMLFactory())
            mapper.findAndRegisterModules()

            return mapper.readValue<T>(ymlStr)
        }

        fun getUriEncoded(uri: String): String {
            return try {
                URLEncoder.encode(uri, "UTF-8")
            } catch (e: Exception) {
                uri
            }
        }
    }
}