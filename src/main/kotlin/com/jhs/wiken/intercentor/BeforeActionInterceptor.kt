package com.jhs.wiken.intercentor

import com.jhs.wiken.service.MemberService
import com.jhs.wiken.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

// 역할 : 모든 액션이 실행되기 전에 필요한 사전준비작업을 수행하기에 적절한 곳
// 주의 : MyWebMvcConfigurer 에서 세팅되지 않으면, 이 클래스는 작동되지 않는다.
@Component
class BeforeActionInterceptor(
    private val rq: Rq,
    private val memberService: MemberService
) : HandlerInterceptor {
    // 모든 액션이 실행되기 전에 실행
    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
        // rq 객체를 세팅
        rq.init()

        if (rq.isLogined) {
            rq.themeName = memberService.getThemeName(rq.loginedMember)
        }

        return super.preHandle(req, resp, handler)
    }
}