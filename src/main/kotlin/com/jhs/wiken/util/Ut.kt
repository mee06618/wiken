package com.jhs.wiken.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.time.LocalDateTime


class Ut {
    companion object {
        val nowDateStr: String
            get() = LocalDateTime.now().toString().replace("T", " ").split(".")[0]

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

        fun getNewUriRemoved(uri: String, paramName: String): String {
            if (uri.isEmpty()) {
                return ""
            }

            var uri = uri
            val deleteStrStarts = "$paramName="
            val delStartPos = uri.indexOf(deleteStrStarts)
            if (delStartPos != -1) {
                var delEndPos = uri.indexOf("&", delStartPos)
                uri = if (delEndPos != -1) {
                    delEndPos++
                    uri.substring(0, delStartPos) + uri.substring(delEndPos, uri.length)
                } else {
                    uri.substring(0, delStartPos)
                }
            }

            if (uri[uri.length - 1] == '?') {
                uri = uri.substring(0, uri.length - 1)
            }
            if (uri[uri.length - 1] == '&') {
                uri = uri.substring(0, uri.length - 1)
            }
            return uri
        }

        fun getNewUri(uri: String, paramName: String, paramValue: String): String? {
            var uri = uri
            uri = getNewUriRemoved(uri, paramName)
            uri += if (uri.contains("?")) {
                "&$paramName=$paramValue"
            } else {
                "?$paramName=$paramValue"
            }
            uri = uri.replace("?&", "?")
            return uri
        }

        fun getNewUriAndEncoded(uri: String, paramName: String, pramValue: String): String? {
            return getUriEncoded(getNewUri(uri, paramName, pramValue)!!)
        }

        fun getTempPassword(length: Int): String {
            var index = 0
            val charArr = charArrayOf(
                '0',
                '1',
                '2',
                '3',
                '4',
                '5',
                '6',
                '7',
                '8',
                '9',
                'a',
                'b',
                'c',
                'd',
                'e',
                'f',
                'g',
                'h',
                'i',
                'j',
                'k',
                'l',
                'm',
                'n',
                'o',
                'p',
                'q',
                'r',
                's',
                't',
                'u',
                'v',
                'w',
                'x',
                'y',
                'z'
            )
            val sb = StringBuffer()
            for (i in 0 until length) {
                index = (charArr.size * Math.random()).toInt()
                sb.append(charArr[index])
            }
            return sb.toString()
        }

        fun getDateStrLater(seconds: Long): String {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return format.format(System.currentTimeMillis() + seconds * 1000)
        }
    }
}