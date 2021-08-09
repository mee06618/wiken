package com.jhs.wiken.repository

import com.jhs.wiken.vo.Member
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface MemberRepository {
    @Select(
        """
        SELECT *
        FROM `member` AS M
        WHERE M.loginId = #{loginId}
    """
    )
    fun getMemberByLoginId(@Param("loginId") loginId: String): Member?
}
