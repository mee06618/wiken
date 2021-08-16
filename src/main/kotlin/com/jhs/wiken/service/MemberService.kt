package com.jhs.wiken.service

import com.jhs.wiken.repository.MemberRepository
import com.jhs.wiken.vo.Member
import com.jhs.wiken.vo.ResultData
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val attrService: AttrService
) {
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

    fun getMemberByEmail(email: String): Member? {
        return memberRepository.getMemberByEmail(email)
    }

    fun changeTheme(actor: Member, themeName: String): ResultData<String> {
        attrService.setValue("member", actor.id, "extra", "themeName", themeName)

        return ResultData.from("S-1", "테마가 변경되었습니다.", "themeName", themeName);
    }

    fun getThemeName(actor: Member): String {
        return attrService.getValue("member", actor.id, "extra", "themeName").ifEmpty { "light" }
    }

    fun modify(id: Int, loginPw: String, email: String): ResultData<Int> {
        memberRepository.modify(id, loginPw, email);

        return ResultData.from("S-1", "회원정보가 수정되었습니다.", "id", id)
    }
}
