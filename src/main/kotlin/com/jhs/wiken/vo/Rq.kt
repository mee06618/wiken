package com.jhs.wiken.vo

import com.jhs.wiken.util.Ut
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

// 역할 : req, session 을 조금 더 쉽게 다룸
// 컴포넌트로 등록, 이름 : rq
// 이름을 정한 이유는, 타임리프에서, ${@rq} 와 같은식으로 접근 할 수 있도록
@Component("rq")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
class Rq {
    private lateinit var req: HttpServletRequest

    // 로그인 된 회원
    private var loginedMember: Member? = null

    // 로그인 여부
    private var isLogined: Boolean = false

    // 현재 페이지에서 Ken 을 저장할 수 있는지 여부
    private var currentPageCanSaveKen = false

    // 현재 페이지에서 현재 Ken을 수정하러 갈 수 있는 버튼이 노출될 수 있는지 여부
    private var currentPageCanGoEditCurrentKen = false

    // 현재 페이지에서 현재 Ken의 상세페이지를 보러 갈 수 있는 버튼이 노출될 수 있는지 여부
    private var currentPageCanGoViewCurrentKen = false

    fun setReq(req: HttpServletRequest) {
        this.req = req

        setLoginInfo(req.session)
    }

    // 로그인 정보를 세션에서 꺼내와서, rq객체에 정보를 세팅
    fun setLoginInfo(session: HttpSession) {
        if (session.getAttribute("loginedMemberJsonStr") == null) {
            return
        }

        val loginedMemberJsonStr = session.getAttribute("loginedMemberJsonStr") as String

        isLogined = true
        loginedMember = Ut.getObjFromJsonStr(loginedMemberJsonStr)
    }

    // 현재 로그인된 회원객체 반환
    // 로그인이 안되어 있을 경우, 호출하면 안됨
    fun getLoginedMember(): Member {
        return loginedMember!!
    }

    // 로그인 되어 있는지 여부 체크
    fun isLogined(): Boolean {
        return this.isLogined
    }

    // currentPageCanSaveKen 관련 함수 시작
    fun setCurrentPageCanSaveKen(can: Boolean) {
        this.currentPageCanSaveKen = can
    }

    fun isCurrentPageCanSaveKen(): Boolean {
        return currentPageCanSaveKen
    }
    // currentPageCanSaveKen 관련 함수 끝

    // currentPageCanGoEditCurrentKen 관련 함수 시작
    fun setCurrentPageCanGoEditCurrentKen(can: Boolean) {
        this.currentPageCanGoEditCurrentKen = can
    }

    fun isCurrentPageCanGoEditCurrentKen(): Boolean {
        return this.currentPageCanGoEditCurrentKen
    }
    // currentPageCanGoEditCurrentKen 관련 함수 끝

    // currentPageCanGoViewCurrentKen 관련 함수 시작
    fun setCurrentPageCanGoViewCurrentKen(can: Boolean) {
        this.currentPageCanGoViewCurrentKen = can
    }

    fun isCurrentPageCanGoViewCurrentKen(): Boolean {
        return this.currentPageCanGoViewCurrentKen
    }
    // currentPageCanGoEditCurrentKen 관련 함수 끝

    // 로그인 처리
    fun login(member: Member) {
        req.session.setAttribute("loginedMemberJsonStr", Ut.getJsonStrFromObj(member))
    }

    // 로그아웃 처리
    fun logout() {
        req.session.removeAttribute("loginedMemberJsonStr")
    }

    // 유틸성
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
}