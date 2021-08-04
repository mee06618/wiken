package com.jhs.wiken.vo

import com.jhs.wiken.util.Ut
import javax.servlet.http.HttpSession

class Rq() {
    private var isLogined: Boolean = false
    private var loginedMember: Member? = null;

    fun isLogined(): Boolean {
        return this.isLogined
    }

    fun getLoginedMember(): Member? {
        return loginedMember
    }

    fun setLoginInfo(session: HttpSession) {
        if (session.getAttribute("loginedMemberJsonStr") == null) {
            return
        }

        val loginedMemberJsonStr = session.getAttribute("loginedMemberJsonStr") as String

        isLogined = true
        loginedMember = Ut.getObjFromJsonStr(loginedMemberJsonStr)
    }
}