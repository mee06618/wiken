package com.jhs.wiken.repository

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface KenRepository {
    @Insert(
        """
        INSERT INTO ken
        SET regDate = NOW(),
        updateDate = NOW(),
        memberId = #{memberId},
        title = #{title},
        `source` = #{source},
        result = #{result},
        typeCode = 'common',
        type2Code = 'markdown'
    """
    )
    fun write(
        @Param("memberId") memberId: Int,
        @Param("title") title: String,
        @Param("source") source: String,
        @Param("result") result: String
    )

    @Select(
        """
        SELECT LAST_INSERT_ID()
    """
    )
    fun getLastInsertId(): Int
}
