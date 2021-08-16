package com.jhs.wiken.service

import com.jhs.wiken.repository.MemberRepository
import com.jhs.wiken.util.Ut
import com.jhs.wiken.vo.Member
import com.jhs.wiken.vo.ResultData
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val attrService: AttrService,
    private val emailService: EmailService
) {
    @Value("\${custom.siteMainUri}")
    private val siteMainUri: String? = null

    @Value("\${custom.siteName}")
    private val siteName: String? = null

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

        val member = getMemberById(id)!!

        notifyEmailVerificationLink(member)

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
        val oldMember = getMemberById(id)!!
        val oldEmail = oldMember.email

        memberRepository.modify(id, loginPw, email)

        val member = getMemberById(id)!!

        if ( oldEmail != email ) {
            notifyEmailVerificationLink(member)
        }

        return ResultData.from("S-1", "회원정보가 수정되었습니다.", "id", id)
    }

    fun getVerifiedEmail(actor: Member): String {
        return attrService.getValue("member", actor.id, "extra", "verifiedEmail").ifEmpty { "" }
    }

    fun notifyEmailVerificationLink(actor: Member) {
        val title = "[${siteName}] 이메일 인증"

        val emailVerificationCode = genEmailVerificationCode(actor)
        val link = "${siteMainUri}/member/doVerifyEmail?code=${emailVerificationCode}&id=${actor.id}&email=${actor.email}"

        val body = """<a href="${link}" target="_blank">${link} 이메일 인증</a>"""

        emailService.send(actor.email, title, body)
    }

    private fun genEmailVerificationCode(actor: Member): String {
        val code = Ut.getTempPassword(6)

        attrService.setValue(
            "member",
            actor.id,
            "extra",
            "emailVerificationCode",
            code,
            Ut.getDateStrLater(60 * 60 * 24)
        )

        return code
    }

    fun checkEmailVerificationCode(id: Int, code: String, email: String): ResultData<*> {
        val emailVerificationCode = attrService.getValue("member", id, "extra", "emailVerificationCode")

        if (code == emailVerificationCode) {
            attrService.setValue("member", id, "extra", "verifiedEmail", email)
            return ResultData.from("S-1", "인증성공")
        }

        return ResultData.from("F-1", "인증코드가 올바르지 않거나 만료되었습니다.")
    }
}
