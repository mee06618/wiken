package com.jhs.wiken.vo

import com.jhs.wiken.util.Ut
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component("rq")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
class Rq(
    val req: HttpServletRequest,
    val resp: HttpServletResponse
) {
    // 로그인 된 회원
    var loginedMember: Member = Member.empty()

    val loginedMemberId: Int
        get() {
            return loginedMember.id
        }

    // 로그인 여부
    var isLogined: Boolean = false

    // 사이트 헤더 타입
    var currentPageSiteHeaderType = "common"

    // 현재 페이지가 토스트 UI 에디터를 사용하는지 여부
    var currentPageUseToastUiEditor = false

    // 현재 페이지에서 Ken 을 저장할 수 있는지 여부
    var currentPageCanSaveKen: Boolean = false

    // 현재 페이지에서 현재 Ken을 수정하러 갈 수 있는 버튼이 노출될 수 있는지 여부
    var currentPageCanGoEditCurrentKen = false

    // 현재 페이지에서 현재 Ken의 상세페이지를 보러 갈 수 있는 버튼이 노출될 수 있는지 여부
    var currentPageCanGoViewCurrentKen = false

    var currentPageCanDeleteCurrentKen = false

    fun init() {
        setCurrentLoginInfo()
    }

    // 로그인 정보를 세션에서 꺼내와서, rq객체에 정보를 세팅
    fun setCurrentLoginInfo() {
        if (req.session.getAttribute("loginedMemberJsonStr") == null) {
            return
        }

        val loginedMemberJsonStr = req.session.getAttribute("loginedMemberJsonStr") as String

        isLogined = true
        loginedMember = Ut.getObjFromJsonStr(loginedMemberJsonStr)
    }

    // 로그인 처리
    fun login(member: Member) {
        req.session.setAttribute("loginedMemberJsonStr", Ut.getJsonStrFromObj(member))
    }

    // 로그아웃 처리
    fun logout() {
        req.session.removeAttribute("loginedMemberJsonStr")
    }

    // 유틸성 시작
    private fun print(str: String) {
        resp.writer.print(str)
    }

    fun replaceJs(msg: String, uri: String): String {
        return """
            <script>
            const msg = '${msg}'.trim();
            
            if ( msg.length > 0 ) {
                alert(msg);
            }
            
            let uri = '${uri}';
            
            uri = uri.replace('JS_UNIX_TIMESTAMP', new Date().getTime());
            
            location.replace(uri);
            </script>
        """.trimIndent()
    }

    fun printReplaceJs(msg: String, uri: String) {
        print(replaceJs(msg, uri))
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

    val currentUri: String
        get() {
            var uri = req.requestURI
            val queryStr = req.queryString
            if (queryStr != null && queryStr.isNotEmpty()) {
                uri += "?$queryStr"
            }
            return uri
        }

    val encodedCurrentUri: String
        get() = Ut.getUriEncoded(currentUri)

    val afterLoginUri: String
        get() {
            val afterLoginUri: String = getStrParam("afterLoginUri", "")
            return afterLoginUri.ifEmpty { currentUri }
        }

    val encodedAfterLoginUri: String
        get() = Ut.getUriEncoded(afterLoginUri)

    fun getStrParam(paramName: String, default: String): String {
        if (req.getParameter(paramName) == null) {
            return default
        }

        return req.getParameter(paramName)
    }

    fun respUtf8() {
        resp.characterEncoding = "UTF-8"
        resp.contentType = "text/html; charset=UTF-8"
    }

    fun historyBackJsOnTemplate(msg: String): String {
        req.setAttribute("historyBack", true)
        req.setAttribute("msg", msg)

        return "common/js"
    }
}