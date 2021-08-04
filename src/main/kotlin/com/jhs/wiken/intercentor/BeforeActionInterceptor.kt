package com.jhs.wiken.intercentor

import com.jhs.wiken.vo.Rq
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class BeforeActionInterceptor : HandlerInterceptor {
    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
        val rq = Rq()

        rq.setLoginInfo(req.session)
        req.setAttribute("rq", rq)

        return super.preHandle(req, resp, handler)
    }
}