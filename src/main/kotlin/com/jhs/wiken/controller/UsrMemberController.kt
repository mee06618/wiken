package com.jhs.wiken.controller

import com.jhs.wiken.service.MemberService
import com.jhs.wiken.vo.ResultData
import com.jhs.wiken.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpSession

@Controller
class UsrMemberController(private val memberService: MemberService) {
    @Autowired
    private lateinit var rq: Rq;

    @RequestMapping("/member/modify")
    fun showModify(): String {
        return "usr/member/modify"
    }

    @RequestMapping("/member/doModify")
    @ResponseBody
    fun doModify(
        loginPw: String,
        email: String,
        @RequestParam(defaultValue = "/ken") replaceUri: String
    ): ResultData<*> {
        if (email.isEmpty()) {
            return ResultData.from("F-1", "이메일을 입력해주세요.")
        }

        val loginedMember = rq.loginedMember
        val modifyRd = memberService.modify(loginedMember.id, loginPw, email)
        val member = memberService.getMemberById(loginedMember.id)!!
        rq.login(member)

        return modifyRd
    }

    @RequestMapping("/member/join")
    fun showJoin(): String {
        return "usr/member/join"
    }

    @RequestMapping("/member/doVerifyEmail")
    @ResponseBody
    fun doVerifyEmail(id: Int, code: String, email: String): String {
        val checkEmailVerificationCodeRd = memberService.checkEmailVerificationCode(id, code, email)

        if (checkEmailVerificationCodeRd.isFail) {
            return rq.replaceJs(checkEmailVerificationCodeRd.msg, "/ken")
        }

        if (rq.isLogined) {
            rq.verifiedEmail = email
        }

        return rq.replaceJs(checkEmailVerificationCodeRd.msg, "/ken")
    }

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
        val cellphoneNo = loginId

        val joinRd = memberService.join(loginId, loginPw, name, nickname, cellphoneNo, email)
        val id = joinRd.data
        val member = memberService.getMemberById(id)!!

        memberService.notifyEmailVerificationLink(member)

        rq.login(member)

        return rq.replaceJs(joinRd.msg, replaceUri)
    }

    @RequestMapping("/member/login")
    fun showLogin(): String {
        return "usr/member/login"
    }

    @RequestMapping("/member/doLogin")
    @ResponseBody
    fun doLogin(loginId: String, loginPw: String, @RequestParam(defaultValue = "/ken") replaceUri: String): String {
        val member = memberService.getMemberByLoginId(loginId)
            ?: return rq.historyBackJs("${loginId}(은)는 존재하지 않는 로그인아이디 입니다.")

        if (member.loginPw != loginPw) {
            return rq.historyBackJs("비밀번호가 일치하지 않습니다.")
        }

        rq.login(member)

        val verifiedEmail = memberService.getVerifiedEmail(member)

        val msg = if (verifiedEmail.isEmpty()) {
            "이메일 인증을 해주세요."
        } else {
            "${member.nickname}님 환영합니다."
        }

        val replaceUri = if (verifiedEmail.isEmpty()) {
            "/member/modify"
        } else {
            replaceUri
        }

        return rq.replaceJs(msg, replaceUri)
    }

    @RequestMapping("/member/doLogout")
    @ResponseBody
    fun doLogout(session: HttpSession): String {
        rq.logout()

        return rq.replaceJs("", "/ken")
    }

    data class DoChangeThemeParam(
        val themeName: String
    )

    @RequestMapping("/member/doChangeTheme")
    @ResponseBody
    fun doChangeTheme(params: DoChangeThemeParam): ResultData<String> {
        val themeName = params.themeName

        rq.themeName = themeName

        return memberService.changeTheme(rq.loginedMember, themeName)
    }
}

