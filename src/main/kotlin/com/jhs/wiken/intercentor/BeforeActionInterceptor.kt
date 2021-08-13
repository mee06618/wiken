package com.jhs.wiken.intercentor

import com.jhs.wiken.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

// 역할 : 모든 액션이 실행되기 전에 필요한 사전준비작업을 수행하기에 적절한 곳
// 주의 : MyWebMvcConfigurer 에서 세팅되지 않으면, 이 클래스는 작동되지 않는다.
@Component
class BeforeActionInterceptor : HandlerInterceptor {
    // 현재 이 프록시 rq 객체는 자세히 설명하자면, 요청마다 새로 만들어지지 않는다.
    // 하지만 rq 객체에 요청을 하면, 결국 요청별로 Rq 객체가 생성되어 작동한다.
    // 즉 우리는 아래 rq 객체가 요청마다 서로 다른 객체라고 인식해도 된다.
    @Autowired
    private lateinit var rq: Rq;

    // 모든 액션이 실행되기 전에 실행
    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
        // rq 객체를 세팅
        rq.init()

        return super.preHandle(req, resp, handler)
    }
}