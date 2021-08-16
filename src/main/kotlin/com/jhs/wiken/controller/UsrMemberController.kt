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

    @RequestMapping("/member/join")
    fun showJoin(): String {
        return "usr/member/join"
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

        oldMember = memberService.getMemberByEmail(email)

        if (oldMember != null) {
            return rq.historyBackJs("이미 사용중인 이메일입니다.")
        }

        val name = loginId
        val nickname = loginId
        val cellphoneNo = loginId

        val joinRd = memberService.join(loginId, loginPw, name, nickname, cellphoneNo, email)
        val id = joinRd.getData()
        val member = memberService.getMemberById(id)!!

        rq.login(member)

        return rq.replaceJs(joinRd.getMsg(), replaceUri)
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

        return rq.replaceJs("${member.nickname}님 환영합니다.", replaceUri)
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
        return memberService.changeTheme(rq.loginedMember, themeName)
    }
}

