package com.jhs.wiken.service

import com.jhs.wiken.repository.MemberRepository
import com.jhs.wiken.vo.Member
import org.springframework.stereotype.Service

@Service
class MemberService(private val memberRepository: MemberRepository) {
    fun getMemberByLoginId(loginId: String): Member? {
        return memberRepository.getMemberByLoginId(loginId)
    }
}
