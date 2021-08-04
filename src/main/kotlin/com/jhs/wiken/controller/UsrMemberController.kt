package com.jhs.wiken.controller

import com.jhs.wiken.util.Ut
import com.jhs.wiken.vo.Member
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpSession

@Controller
class UsrMemberController {
    @RequestMapping("/usr/member/doLogin")
    @ResponseBody
    fun doLogin(loginId: String, loginPw: String, session: HttpSession): String {
        val member = Member(1, "2021-12-12 12:12:12", "2021-12-12 12:12:12", "user1", "user1")

        session.setAttribute("loginedMemberJsonStr", Ut.getJsonStrFromObj(member))

        return "로그인 성공"
    }

    @RequestMapping("/usr/member/doLogout")
    @ResponseBody
    fun doLogout(session: HttpSession): String {
        session.removeAttribute("loginedMemberJsonStr")

        return "로그아웃 성공"
    }
}