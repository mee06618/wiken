package com.jhs.wiken.service

import com.jhs.wiken.repository.MemberRepository
import com.jhs.wiken.vo.Member
import com.jhs.wiken.vo.ResultData
import org.springframework.stereotype.Service

@Service
class MemberService(private val memberRepository: MemberRepository) {
    fun getMemberByLoginId(loginId: String): Member? {
        return memberRepository.getMemberByLoginId(loginId)
    }

    fun getMemberById(id: Int): Member? {
        return memberRepository.getMemberById(id)
    }

    fun join(
        loginId: String,
        loginPw: String,
        name: String,
        nickname: String,
        cellphoneNo: String,
        email: String
    ): ResultData<Int> {
        memberRepository.join(loginId, loginPw, name, nickname, cellphoneNo, email)
        val id = memberRepository.getLastInsertId()

        return ResultData.from("S-1", "${nickname}님 환영합니다.", "id", id)
    }
}
