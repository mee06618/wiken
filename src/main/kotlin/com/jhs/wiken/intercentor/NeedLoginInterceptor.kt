package com.jhs.wiken.intercentor

import com.jhs.wiken.util.Ut
import com.jhs.wiken.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class NeedLoginInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var rq: Rq;

    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
        if (!rq.isLogined) {
            rq.respUtf8()
            rq.printReplaceJs(
                "",
                "/member/login?afterLoginUri=${rq.encodedAfterLoginUri}&toastMsg=로그인 후 이용해주세요.&toastMsgJsUnixTimestamp=JS_UNIX_TIMESTAMP"
            )

            return false
        }

        return super.preHandle(req, resp, handler)
    }
}