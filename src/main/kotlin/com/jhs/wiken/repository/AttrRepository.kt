package com.jhs.wiken.repository

import com.jhs.wiken.vo.Attr
import org.apache.ibatis.annotations.*

@Mapper
interface AttrRepository {
    @Select(
        """
        SELECT AT.*
		FROM attr AS AT
		WHERE AT.relId = #{relId}
		AND AT.relTypeCode = #{relTypeCode}
		AND AT.typeCode = #{typeCode}
		AND AT.type2Code = #{type2Code}
		AND (AT.expireDate >= NOW() OR AT.expireDate IS NULL)
    """
    )
    fun get(
        @Param("relTypeCode") relTypeCode: String,
        @Param("relId") relId: Int,
        @Param("typeCode") typeCode: String,
        @Param("type2Code") type2Code: String
    ): Attr?

    @Delete(
        """
        DELETE FROM attr
        WHERE relId = #{relId}
        AND relTypeCode = #{relTypeCode}
        AND typeCode = #{typeCode}
        AND type2Code = #{type2Code}
    """
    )
    fun remove(
        @Param("relTypeCode") relTypeCode: String,
        @Param("relId") relId: Int,
        @Param("typeCode") typeCode: String,
        @Param("type2Code") type2Code: String
    )

    @Insert(
        """
        INSERT INTO attr (regDate, updateDate, `relTypeCode`, `relId`, `typeCode`, `type2Code`, `value`, expireDate)
        VALUES (NOW(), NOW(), #{relTypeCode}, #{relId}, #{typeCode}, #{type2Code}, #{value}, #{expireDate})
        ON DUPLICATE KEY UPDATE
        updateDate = NOW() , `value` = #{value}, expireDate = #{expireDate}
    """
    )
    fun setValue(
        @Param("relTypeCode") relTypeCode: String,
        @Param("relId") relId: Int,
        @Param("typeCode") typeCode: String,
        @Param("type2Code") type2Code: String,
        @Param("value") value: String,
        @Param("expireDate") expireDate: String?
    )

    @Select(
        """
        SELECT value
		FROM attr
		WHERE relId = #{relId}
		AND relTypeCode = #{relTypeCode}
		AND typeCode = #{typeCode}
		AND type2Code = #{type2Code}
		AND (expireDate >= NOW() OR expireDate IS NULL)
    """
    )
    fun getValue(
        @Param("relTypeCode") relTypeCode: String,
        @Param("relId") relId: Int,
        @Param("typeCode") typeCode: String,
        @Param("type2Code") type2Code: String
    ): String
}
