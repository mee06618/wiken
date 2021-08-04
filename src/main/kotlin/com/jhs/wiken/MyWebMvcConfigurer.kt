package com.jhs.wiken

import com.jhs.wiken.intercentor.BeforeActionInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class MyWebMvcConfigurer(
    private val beforeActionInterceptor: BeforeActionInterceptor
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(beforeActionInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/resource/**")
            .excludePathPatterns("/error")
    }
}
