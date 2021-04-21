package com.gdh.shoppingmall.config

import com.gdh.shoppingmall.interceptor.TokenValidationInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * 준비된 인터셉터를 스프링에서 사용할 수 있게 등록
 *
 * @Configuration -> 이 클래스가 스프링에서 사용하는 설정을 담은 빈 클래스라는 것을 나타낸다.
 *                   스프링이 SpringBootApplication 이하의 패키지에서 모든 설정 클래스들을 검사해 자동으로 빈을 생성해준다.
 *
 * WebMvcConfigurer -> 스프링 MVC 프로젝트에서 네이티브 코드 베이스로 설정을 입력할 수 있게 해주는 여러 가지의 콜백 함수들이 정의된 인터페이스
 * addInterceptors() -> 인터셉터들을 추가할 수 있는 InterceptorRegistry 객체를 파라미터로 전달,
 *                      /api/ 이하의 URI에서 인터셉터가 동작하도록 설정
 */

@Configuration
class WebConfig @Autowired constructor(
    private val tokenValidationInterceptor: TokenValidationInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(tokenValidationInterceptor)
            .addPathPatterns("/api/**")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/images/**")
            .addResourceLocations("file:///parayo/images")
    }
}