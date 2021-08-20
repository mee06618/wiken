package com.jhs.wiken.service

import com.jhs.wiken.repository.MemberRepository
import com.jhs.wiken.util.Ut
import com.jhs.wiken.vo.Attr
import com.jhs.wiken.vo.Ken
import com.jhs.wiken.vo.Member
import com.jhs.wiken.vo.ResultData
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

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

    // 완벽
    fun getMemberByLoginId(loginId: String): Member? {
        return memberRepository.getMemberByLoginId(loginId)
    }

    // 완벽
    fun getMemberById(id: Int): Member? {
        return memberRepository.getMemberById(id)
    }

    // 완벽
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

    // 완벽
    fun getMemberByEmail(email: String): Member? {
        return memberRepository.getMemberByEmail(email)
    }

    // 완벽
    fun changeTheme(actor: Member, themeName: String): ResultData<String> {
        attrService.setValue("member", actor.id, "extra", "themeName", themeName)

        return ResultData.from("S-1", "테마가 변경되었습니다.", "themeName", themeName);
    }

    // 완벽
    fun getThemeName(actor: Member): String {
        return attrService.getValue("member", actor.id, "extra", "themeName").ifEmpty { "light" }
    }

    // 완벽
    fun modify(id: Int, loginPw: String, email: String): ResultData<Int> {
        val oldMember = getMemberById(id)!!
        val oldEmail = oldMember.email

        memberRepository.modify(id, loginPw, email)

        val member = getMemberById(id)!!

        // 이메일이 달라졌다면
        if (oldEmail != email) {
            // 기존의 인증된 메일정보는 삭제한다.
            removeVerifiedEmail(member)
            notifyEmailVerificationLink(member)
        }

        return ResultData.from("S-1", "회원정보가 수정되었습니다.", "id", id)
    }

    // 완벽
    fun getVerifiedEmail(actor: Member): String {
        return attrService.getValue("member", actor.id, "extra", "verifiedEmail").ifEmpty { "" }
    }

    // 완벽
    fun removeVerifiedEmail(actor: Member) {
        return attrService.remove("member", actor.id, "extra", "verifiedEmail")
    }

    // 완벽
    fun getEmailVerificationCodeAttr(actor: Member): Attr? {
        return attrService.get("member", actor.id, "extra", "emailVerificationCode")
    }

    // 완벽
    fun notifyEmailVerificationLink(actor: Member): ResultData<String> {
        val verifiedEmail = getVerifiedEmail(actor)

        if (verifiedEmail == actor.email) {
            return ResultData.from("F-3", "이미 인증된 이메일 입니다.")
        }

        val attr = getEmailVerificationCodeAttr(actor)

        if (attr != null) {
            val updateLocalDateTime = Ut.localDateTimeFromStr(attr.updateDate)
            val minutes = ChronoUnit.MINUTES.between(updateLocalDateTime, LocalDateTime.now())
            val minDelayMinutes = 5
            if (minutes < minDelayMinutes) {
                val restMinutes = minDelayMinutes - minutes
                return ResultData.from("F-2", "이미 링크가 이메일로 발송되었습니다. 링크 재발송은 ${restMinutes}분 뒤에 가능합니다.")
            }
        }

        val title = "[${siteName}] 이메일 인증"

        val emailVerificationCode = genEmailVerificationCode(actor)
        val link =
            "${siteMainUri}/member/doVerifyEmail?code=${emailVerificationCode}&id=${actor.id}&email=${actor.email}"

        val body = """이메일 인증 : <a href="${link}" target="_blank">${link}</a>"""

        return emailService.send(actor.email, title, body)
    }

    // 완벽
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

    // 완벽
    fun genVerifiedEmail(id: Int, email: String): ResultData<*> {
        attrService.setValue("member", id, "extra", "verifiedEmail", email)

        return ResultData.from("S-1", "인증된 메일로 저장했습니다.")
    }

    // 완벽
    fun checkEmailVerificationCode(id: Int, code: String, email: String): ResultData<*> {
        val emailVerificationCode = attrService.getValue("member", id, "extra", "emailVerificationCode")

        if (code == emailVerificationCode) {
            return ResultData.from("S-1", "인증성공")
        }

        return ResultData.from("F-1", "인증코드가 올바르지 않거나 만료되었습니다.")
    }

    // 완벽
    // 패스워드 재설정 링크를 메일로 보낸다.
    fun notifyPasswordResetLink(actor: Member): ResultData<*> {
        val attr = getPasswordResetAuthCodeAttr(actor)

        if (attr != null) {
            val updateLocalDateTime = Ut.localDateTimeFromStr(attr.updateDate)
            val minutes = ChronoUnit.MINUTES.between(updateLocalDateTime, LocalDateTime.now())
            val minDelayMinutes = 5
            if (minutes < minDelayMinutes) {
                val restMinutes = minDelayMinutes - minutes
                return ResultData.from("F-2", "이미 링크가 이메일로 발송되었습니다. 링크 재발송은 ${restMinutes}분 뒤에 가능합니다.")
            }
        }

        val title = "[${siteName}] 비밀번호 변경"

        val passwordResetAuthCode = genPasswordResetAuthCode(actor)
        val link =
            "${siteMainUri}/member/modifyPasswordByResetAuthCode?code=${passwordResetAuthCode}&id=${actor.id}&email=${actor.email}"

        val body = """비밀번호 변경 : <a href="${link}" target="_blank">${link}</a>"""

        val sendRd = emailService.send(actor.email, title, body)

        if (sendRd.isFail) {
            return sendRd
        }

        return ResultData.from("S-1", "해당 메일로 비밀번호 변경가능한 링크가 발송되었습니다.")
    }

    // 완벽
    private fun genPasswordResetAuthCode(actor: Member): Any {
        val code = Ut.getTempPassword(6)

        attrService.setValue(
            "member",
            actor.id,
            "extra",
            "passwordResetAuthCode",
            code,
            Ut.getDateStrLater(60 * 60 * 24)
        )

        return code
    }

    // 완벽
    fun getPasswordResetAuthCodeAttr(actor: Member): Attr? {
        return attrService.get(
            "member",
            actor.id,
            "extra",
            "passwordResetAuthCode",
        )
    }

    // 완벽
    fun checkPasswordResetAuthCode(id: Int, code: String): ResultData<*> {
        val checkPasswordResetAuthCode = attrService.getValue("member", id, "extra", "passwordResetAuthCode")

        if (code == checkPasswordResetAuthCode) {
            return ResultData.from("S-1", "인증성공")
        }

        return ResultData.from("F-1", "인증코드가 올바르지 않거나 만료되었습니다.")
    }

    fun actorCanModify(actor: Member, ken: Ken): ResultData<*> {
        if ( actor.id != ken.memberId ) {
            return ResultData.from("F-1", "권한이 없습니다.")
        }

        return ResultData.from("S-1", "수정할 수 있습니다.")
    }

    fun actorCanDelete(actor: Member, ken: Ken): ResultData<*> {
        if ( actor.id != ken.memberId ) {
            return ResultData.from("F-1", "권한이 없습니다.")
        }

        return ResultData.from("S-1", "삭제할 수 있습니다.")
    }
}
