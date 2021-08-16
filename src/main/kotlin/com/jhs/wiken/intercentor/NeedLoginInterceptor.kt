package com.jhs.wiken.intercentor

import com.jhs.wiken.vo.ResultData
import com.jhs.wiken.vo.Rq
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class NeedLoginInterceptor(
    private val rq: Rq
) : HandlerInterceptor {
    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
        if (!rq.isLogined) {
            if (rq.isAjax) {
                rq.respUtf8Json()
                rq.printJson(ResultData.from("F-A", "로그인 후 이용해주세요."))
            } else {
                rq.respUtf8()
                rq.printReplaceJs(
                    "로그인 후 이용해주세요.",
                    "/member/login?afterLoginUri=${rq.encodedAfterLoginUri}"
                )
            }

            return false
        }

        return super.preHandle(req, resp, handler)
    }
}