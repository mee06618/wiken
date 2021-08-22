package com.jhs.wiken.vo

import com.jhs.wiken.service.MemberService
import com.jhs.wiken.util.Ut
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

// 이렇게 컴포넌트로 지정하면서, rq 라는 이름까지 지정하면, 타임리프에서 ${@rq.~~} 와 같은 식으로 접근가능
@Component("rq")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
class Rq(
    private val req: HttpServletRequest,
    private val resp: HttpServletResponse,
    private val memberService: MemberService
) {
    // 완벽
    // 인증된 이메일
    var verifiedEmail: String
        get() {
            if (isLogined) {
                return req.session.getAttribute("verifiedEmail") as String
            }

            return ""
        }
        set(value) {
            req.session.setAttribute("verifiedEmail", value)
        }

    // 완벽
    // 사용중인 테마이름
    var themeName: String
        get() {
            if (isLogined) {
                return req.session.getAttribute("themeName") as String
            }

            return "mylight"
        }
        set(value) {
            req.session.setAttribute("themeName", value)
        }

    // 완벽
    // 각 헤더메뉴 아이템들의 인디케이터 텍스트
    val headerMenuItemsIndicatorText = mutableMapOf<String, String>()

    // 완벽
    // 이 변수를 직접 참조하지 마세요.
    private var _loginedMember: Member? = null;

    // 완벽
    // 로그인 된 회원
    val loginedMember: Member by lazy {
        if (_loginedMember != null) {
            _loginedMember!!
        } else {
            Member.empty()
        }
    }

    val loginedMemberId: Int
        get() {
            return loginedMember.id
        }

    // 완벽
    // 로그인 여부
    val isLogined: Boolean by lazy {
        req.session.getAttribute("loginedMemberJsonStr") != null
    }

    // 완벽
    val isNotLogined: Boolean by lazy {
        !isLogined
    }

    // 완벽
    // 현재 요청이 ajax 인지
    val isAjax by lazy {
        req.getParameter("ajaxMode") != null && req.getParameter("ajaxMode") == "Y"
    }

    // 완벽
    // 사이트 헤더 타입
    var currentPageSiteHeaderType = "common"

    // 완벽
    // 현재 페이지가 토스트 UI 에디터를 사용하는지 여부
    var currentPageUseToastUiEditor = false

    // 완벽
    // 현재 페이지에서 Ken 을 저장할 수 있는지 여부
    var currentPageCanSaveKen = false

    // 완벽
    // 현재 페이지에서 현재 Ken을 수정하러 갈 수 있는 버튼이 노출될 수 있는지 여부
    var currentPageCanGoEditCurrentKen = false

    // 완벽
    // 현재 페이지에서 현재 Ken의 상세페이지를 보러 갈 수 있는 버튼이 노출될 수 있는지 여부
    var currentPageCanGoViewCurrentKen = false

    // 완벽
    // 현재 페이지에서 현재 Ken을 삭제할 수 있는지 여부
    var currentPageCanDeleteCurrentKen = false

    // 완벽
    fun init() {
        // 로그인 되어있다면, 로그인된 사용자 정보를 세션에서 가져온다.
        loadLoginedMemberInfoFromSessionIfLogined()

        // 상단바 메뉴 아이템들의 인디케이터 텍스트 업데이트
        updateHeaderMenuItemsIndicatorText()
    }

    // 완벽
    fun updateHeaderMenuItemsIndicatorText() {
        // 이메일인증이 안되었다면
        if (verifiedEmail.isEmpty()) {
            // 마이페이지에 1 표시
            headerMenuItemsIndicatorText["myPage"] = "1"
        }
    }

    // 완벽
    // 로그인 정보를 세션에서 꺼내와서, rq객체에 정보를 세팅
    fun loadLoginedMemberInfoFromSessionIfLogined() {
        if (isNotLogined) {
            return
        }

        val loginedMemberJsonStr = req.session.getAttribute("loginedMemberJsonStr") as String
        _loginedMember = Ut.getObjFromJsonStr(loginedMemberJsonStr)
    }

    // 완벽
    // 로그인에 관련된 정보를 세션에 생성한다.
    fun genLoginInfoOnSession(member: Member) {
        req.session.setAttribute("loginedMemberJsonStr", Ut.getJsonStrFromObj(member))
        themeName = memberService.getThemeName(member)
        verifiedEmail = memberService.getVerifiedEmail(member)
    }

    // 완벽
    // 로그아웃 처리
    fun clearLoginInfoOnSession() {
        req.session.removeAttribute("loginedMemberJsonStr")
        req.session.removeAttribute("verifiedEmail")
        req.session.removeAttribute("themeName")
    }

    // 완벽
    // 유틸성 시작
    private fun print(str: String) {
        resp.writer.print(str)
    }

    // 완벽
    fun replaceJs(msg: String, uri: String): String {
        var _uri = Ut.getNewUriRemoved(uri, "toastMsg")
        _uri = Ut.getNewUriRemoved(_uri, "toastMsgJsUnixTimestamp")
        return """
            <script>
            let uri = '${_uri}';
            
            const msg = '${msg}'.trim();
            
            if ( msg.length > 0 ) {
                if ( uri.indexOf('?') !== -1 ) {
                    uri += '&';
                }
                else {
                    uri += '?';
                }
                
                uri += 'toastMsg=' + msg + '&toastMsgJsUnixTimestamp=JS_UNIX_TIMESTAMP';
            }
            
            uri = uri.replace('JS_UNIX_TIMESTAMP', new Date().getTime());
            
            location.replace(uri);
            </script>
        """.trimIndent()
    }

    // 완벽
    fun printReplaceJs(msg: String, uri: String) {
        print(replaceJs(msg, uri))
    }

    // 완벽
    fun historyBackJs(msg: String): String {
        return """
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <!-- 제이쿼리 -->
            <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
            
            <!-- 토스틀, alert 와 confirm을 대체할 목적으로 사용 -->
            <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/2.1.4/toastr.min.js"></script>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/2.1.4/toastr.min.css">
            
            <script src="/resource/toastr_custom.js"></script>
            
            <style>
            .toast-center-center {
                top:50%;
                left:50%;
                transform:translate(-50%, -50%);
            }
            </style>
            
            <script>
            const msg = '${msg}'.trim();
            
            if ( msg.length > 0 ) {
                toastr.options.onHidden = function() { history.back(); }
                toastr.options.positionClass = "toast-center-center";
                setTimeout(function() {
                    toastWarning(msg);
                }, 100);
            }
            else {
                history.back();
            }
            </script>
        """.trimIndent()
    }

    val currentUri: String by lazy {
        var uri = req.requestURI
        val queryStr = req.queryString
        if (queryStr != null && queryStr.isNotEmpty()) {
            uri += "?${queryStr}"
        }

        uri
    }

    // 완벽
    val encodedCurrentUri by lazy {
        Ut.getUriEncoded(currentUri)
    }

    // 완벽
    val afterLoginUri: String by lazy {
        var afterLoginUri: String = getStrParam("afterLoginUri", "")

        if (afterLoginUri.isEmpty()) {
            currentUri
        } else {
            afterLoginUri = Ut.getNewUriRemoved(afterLoginUri, "toastMsg")
            afterLoginUri = Ut.getNewUriRemoved(afterLoginUri, "toastMsgJsUnixTimestamp")

            afterLoginUri
        }
    }

    // 완벽
    val encodedAfterLoginUri: String by lazy {
        Ut.getUriEncoded(afterLoginUri)
    }

    // 완벽
    fun getStrParam(paramName: String, default: String): String {
        if (req.getParameter(paramName) == null) {
            return default
        }

        return req.getParameter(paramName)
    }

    // 완벽
    fun respUtf8() {
        resp.characterEncoding = "UTF-8"
        resp.contentType = "text/html; charset=UTF-8"
    }

    // 완벽
    fun respUtf8Json() {
        resp.characterEncoding = "UTF-8"
        resp.contentType = "application/json; charset=UTF-8"
    }

    // 완벽
    fun historyBackJsOnTemplate(msg: String): String {
        req.setAttribute("historyBack", true)
        req.setAttribute("msg", msg)

        return "common/js"
    }

    // 완벽
    fun printJson(resultData: ResultData<String>) {
        print(Ut.getJsonStrFromObj(resultData))
    }

    // 완벽
    fun regenLoginInfoOnSession() {
        val member = memberService.getMemberById(loginedMemberId)!!
        genLoginInfoOnSession(member)
    }
}