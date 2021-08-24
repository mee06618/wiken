package com.jhs.wiken.controller

import com.jhs.wiken.service.MemberService
import com.jhs.wiken.vo.ResultData
import com.jhs.wiken.vo.Rq
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpSession


@Controller
class UsrMemberController(
    private val memberService: MemberService,
    private val rq: Rq
) {
    // 완벽
    @RequestMapping("/member/modify")
    fun showModify(): String {
        return "usr/member/modify"
    }

    // 완벽
    @RequestMapping("/member/doModify")
    @ResponseBody
    fun doModify(
        loginPw: String,
        email: String
    ): ResultData<*> {
        if (email.isEmpty()) {
            return ResultData.from("F-1", "이메일을 입력해주세요.")
        }

        val modifyRd = memberService.modify(rq.loginedMemberId, loginPw, email)

        // 세션에 저장되어 있는 로그인된 회원의 정보를 재생성한다.
        // 왜냐하면 방금 처리로 인해 회원정보가 수정되었기 때문이다.
        rq.regenLoginInfoOnSession();

        return modifyRd
    }

    // 완벽
    @RequestMapping("/member/join")
    fun showJoin(): String {
        return "usr/member/join"
    }

    // 완벽
    @RequestMapping("/member/doVerifyEmail")
    @ResponseBody
    fun doVerifyEmail(id: Int, code: String, email: String): String {
        val checkEmailVerificationCodeRd = memberService.checkEmailVerificationCode(id, code, email)

        if (checkEmailVerificationCodeRd.isFail) {
            return rq.replaceJs(checkEmailVerificationCodeRd.msg, "/ken")
        }

        memberService.genVerifiedEmail(id, email)

        if (rq.isLogined) {
            rq.regenLoginInfoOnSession()
        }

        return rq.replaceJs(checkEmailVerificationCodeRd.msg, "/ken")
    }

    // 완벽
    @RequestMapping("/member/doJoin")
    @ResponseBody
    fun doJoin(
        loginId: String,
        loginPw: String,
        email: String,
        @RequestParam(defaultValue = "/ken") replaceUri: String
    ): String {
        var oldMember = memberService.getMemberByLoginId(loginId)

        if (oldMember != null) {
            return rq.historyBackJs("이미 사용중인 로그인 아이디 입니다.")
        }

        if (email.isEmpty()) {
            return rq.historyBackJs("이메일을 입력해주세요.")
        }

        oldMember = memberService.getMemberByEmail(email)

        if (oldMember != null) {
            return rq.historyBackJs("이미 사용중인 이메일입니다.")
        }

        val name = loginId
        val nickname = loginId
        val cellphoneNo = ""

        val joinRd = memberService.join(loginId, loginPw, name, nickname, cellphoneNo, email)
        val id = joinRd.data
        val member = memberService.getMemberById(id)!!

        rq.genLoginInfoOnSession(member)

        return rq.replaceJs(joinRd.msg, replaceUri)
    }

    // 완벽
    @RequestMapping("/member/findLoginId")
    fun showFindLoginId(): String {
        return "usr/member/findLoginId"
    }

    // 완벽
    @RequestMapping("/member/doFindLoginId")
    @ResponseBody
    fun doFindLoginId(email: String): ResultData<*> {
        val member = memberService.getMemberByEmail(email)
            ?: return ResultData.from("F-1", "일치하는 아이디가 존재하지 않습니다.")

        return ResultData.from("S-1", "해당 회원의 로그인아이디는 ${member.loginId} 입니다.", "loginId", member.loginId)
    }

    // 완벽
    @RequestMapping("/member/doResendEmailVerificationLink")
    @ResponseBody
    fun doResendEmailVerificationLink(): ResultData<*> {
        return memberService.notifyEmailVerificationLink(rq.loginedMember)
    }

    // 완벽
    @RequestMapping("/member/findLoginPw")
    fun showFindLoginPw(): String {
        return "usr/member/findLoginPw"
    }

    // 완벽
    @RequestMapping("/member/doFindLoginPw")
    @ResponseBody
    fun doFindLoginPw(email: String): ResultData<*> {
        val member = memberService.getMemberByEmail(email)
            ?: return ResultData.from("F-1", "일치하는 아이디가 존재하지 않습니다.")

        return memberService.notifyPasswordResetLink(member)
    }

    // 완벽
    @RequestMapping("/member/modifyPasswordByResetAuthCode")
    fun showModifyPasswordByResetAuthCode(id: Int, email: String, code: String, model: Model): String {
        val checkPasswordResetAuthCodeRd = memberService.checkPasswordResetAuthCode(id, code)

        if (checkPasswordResetAuthCodeRd.isFail) {
            model["errorMsg"] = checkPasswordResetAuthCodeRd.msg
        }

        val member = memberService.getMemberById(id)!!

        model["member"] = member

        return "usr/member/modifyPasswordByResetAuthCode"
    }

    // 완벽
    @RequestMapping("/member/doModifyPasswordByResetAuthCode")
    @ResponseBody
    fun doModifyPasswordByResetAuthCode(id: Int, email: String, code: String, loginPw: String): ResultData<*> {
        val checkPasswordResetAuthCodeRd = memberService.checkPasswordResetAuthCode(id, code)

        if (checkPasswordResetAuthCodeRd.isFail) {
            return checkPasswordResetAuthCodeRd
        }

        return memberService.modify(id, loginPw, "")
    }

    // 완벽
    @RequestMapping("/member/login")
    fun showLogin(): String {
        return "usr/member/login"
    }

    // 완벽
    @RequestMapping("/member/doLogin")
    @ResponseBody
    fun doLogin(loginId: String, loginPw: String): ResultData<*> {
        val member = memberService.getMemberByLoginId(loginId)
            ?: return ResultData.from("F-1", "${loginId}(은)는 존재하지 않는 로그인아이디 입니다.")

        if (member.loginPw != loginPw) {
            return ResultData.from("F-2", "비밀번호가 일치하지 않습니다.")
        }

        rq.genLoginInfoOnSession(member)

        val verifiedEmail = memberService.getVerifiedEmail(member)

        val msg = if (verifiedEmail.isEmpty()) {
            "이메일 인증을 해주세요."
        } else {
            "${member.nickname}님 환영합니다."
        }

        return ResultData.from("S-1", msg)
    }

    @RequestMapping("/member/doLogout")
    @ResponseBody
    fun doLogout(session: HttpSession, replaceUri: String = "/ken"): String {
        rq.clearLoginInfoOnSession()

        return rq.replaceJs("", replaceUri)
    }

    // 완벽
    data class DoChangeThemeParam(
        val themeName: String
    )

    // 완벽
    @RequestMapping("/member/doChangeTheme")
    @ResponseBody
    fun doChangeTheme(params: DoChangeThemeParam): ResultData<String> {
        val themeName = params.themeName

        rq.themeName = themeName

        return memberService.changeTheme(rq.loginedMember, themeName)
    }
}

