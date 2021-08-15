package com.jhs.wiken.service

import com.jhs.wiken.repository.AttrRepository
import com.jhs.wiken.vo.Attr
import org.springframework.stereotype.Service

@Service
class AttrService(private val attrDao: AttrRepository) {
    operator fun get(name: String): Attr? {
        val nameBits = name.split("__").toTypedArray()
        val relTypeCode = nameBits[0]
        val relId = nameBits[1].toInt()
        val typeCode = nameBits[2]
        val type2Code = nameBits[3]
        return get(relTypeCode, relId, typeCode, type2Code)
    }

    fun get(relTypeCode: String, relId: Int, typeCode: String, type2Code: String): Attr? {
        return attrDao.get(relTypeCode, relId, typeCode, type2Code)
    }

    fun setValue(name: String, value: String, expireDate: String? = null) {
        val nameBits = name.split("__").toTypedArray()
        val relTypeCode = nameBits[0]
        val relId = nameBits[1].toInt()
        val typeCode = nameBits[2]
        val type2Code = nameBits[3]
        return setValue(relTypeCode, relId, typeCode, type2Code, value, expireDate)
    }

    fun getValue(name: String): String {
        val nameBits = name.split("__").toTypedArray()
        val relTypeCode = nameBits[0]
        val relId = nameBits[1].toInt()
        val typeCode = nameBits[2]
        val type2Code = nameBits[3]
        return getValue(relTypeCode, relId, typeCode, type2Code)
    }

    fun getValue(
        relTypeCode: String,
        relId: Int,
        typeCode: String,
        type2Code: String
    ): String {
        return attrDao.getValue(relTypeCode, relId, typeCode, type2Code) ?: return ""
    }

    fun remove(name: String) {
        val nameBits = name.split("__").toTypedArray()
        val relTypeCode = nameBits[0]
        val relId = nameBits[1].toInt()
        val typeCode = nameBits[2]
        val type2Code = nameBits[3]
        return remove(relTypeCode, relId, typeCode, type2Code)
    }

    fun remove(relTypeCode: String, relId: Int, typeCode: String, type2Code: String) {
        return attrDao.remove(relTypeCode, relId, typeCode, type2Code)
    }

    fun setValue(
        relTypeCode: String,
        relId: Int,
        typeCode: String,
        type2Code: String,
        value: String,
        expireDate: String? = null
    ) {
        attrDao.setValue(relTypeCode, relId, typeCode, type2Code, value, expireDate)
    }
}