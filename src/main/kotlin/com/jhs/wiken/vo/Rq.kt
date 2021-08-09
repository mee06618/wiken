package com.jhs.wiken.vo

import com.jhs.wiken.util.Ut
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Component("rq")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
class Rq {
    private var currentPageCanGoEditCurrentKen: Boolean = false
    private lateinit var req: HttpServletRequest
    private var isLogined: Boolean = false
    private var loginedMember: Member? = null
    private var currentPageCanSaveKen = false

    fun isLogined(): Boolean {
        return this.isLogined
    }

    fun getLoginedMember(): Member? {
        return loginedMember
    }

    fun setCurrentPageCanSaveKen(can: Boolean) {
        this.currentPageCanSaveKen = can
    }

    fun isCurrentPageCanSaveKen(): Boolean {
        return currentPageCanSaveKen
    }

    fun setLoginInfo(session: HttpSession) {
        if (session.getAttribute("loginedMemberJsonStr") == null) {
            return
        }

        val loginedMemberJsonStr = session.getAttribute("loginedMemberJsonStr") as String

        isLogined = true
        loginedMember = Ut.getObjFromJsonStr(loginedMemberJsonStr)
    }

    fun replaceJs(msg: String, uri: String): String {
        return """
            <script>
            const msg = '${msg}'.trim();
            
            if ( msg.length > 0 ) {
                alert(msg);
            }
            
            location.replace('${uri}');
            </script>
        """.trimIndent()
    }

    fun historyBackJs(msg: String): String {
        return """
            <script>
            const msg = '${msg}'.trim();
            
            if ( msg.length > 0 ) {
                alert(msg);
            }
            
            history.back();
            </script>
        """.trimIndent()
    }

    fun setReq(req: HttpServletRequest) {
        this.req = req

        setLoginInfo(req.session)
    }

    fun setCurrentPageCanGoEditCurrentKen(can: Boolean) {
        this.currentPageCanGoEditCurrentKen = can
    }

    fun isCurrentPageCanGoEditCurrentKen(): Boolean {
        return this.currentPageCanGoEditCurrentKen
    }

    fun login(member: Member) {
        req.session.setAttribute("loginedMemberJsonStr", Ut.getJsonStrFromObj(member))
    }

    fun logout() {
        req.session.removeAttribute("loginedMemberJsonStr")
    }
}