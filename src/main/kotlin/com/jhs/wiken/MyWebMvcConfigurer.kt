package com.jhs.wiken

import com.jhs.wiken.intercentor.BeforeActionInterceptor
import com.jhs.wiken.intercentor.NeedLoginInterceptor
import com.jhs.wiken.intercentor.NeedLogoutInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

// 역할 : 앱의 복잡한 세팅을 담당, 간단한 세팅은 application.yml이 담당
@Configuration
class MyWebMvcConfigurer(
    private val beforeActionInterceptor: BeforeActionInterceptor,
    private val needLoginInterceptor: NeedLoginInterceptor,
    private val needLogoutInterceptor: NeedLogoutInterceptor
) : WebMvcConfigurer {
    // 이 함수는 앱이 실행되고 나서 딱 1번 실행된다. 즉 요청마다 실행되지 않는다.
    override fun addInterceptors(registry: InterceptorRegistry) {
        // 인터셉터에게 어떤 요청이 들어왔을 때 일 해야하는지 알려준다.
        registry.addInterceptor(beforeActionInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/resource/**")
            .excludePathPatterns("/error")

        registry.addInterceptor(needLoginInterceptor)
            .addPathPatterns("/member/doChangeTheme")
            .addPathPatterns("/member/checkPassword")
            .addPathPatterns("/member/doCheckPassword")
            .addPathPatterns("/member/modify")
            .addPathPatterns("/member/doModify")
            .addPathPatterns("/ken")
            .addPathPatterns("/ken/{id}/edit")
            .addPathPatterns("/ken/doWrite")
            .addPathPatterns("/ken/doModify")
            .addPathPatterns("/member/doResendEmailVerificationLink")

        registry.addInterceptor(needLogoutInterceptor)
            .addPathPatterns("/member/login")
            .addPathPatterns("/member/doLogin")
            .addPathPatterns("/member/join")
            .addPathPatterns("/member/doJoin")
            .addPathPatterns("/member/doFindLoginPw")
            .addPathPatterns("/member/checkPassword")
            .addPathPatterns("/member/doCheckPassword")
            .addPathPatterns("/member/modifyPasswordByResetAuthCode")
            .addPathPatterns("/member/doModifyPasswordByResetAuthCode")
    }
}
