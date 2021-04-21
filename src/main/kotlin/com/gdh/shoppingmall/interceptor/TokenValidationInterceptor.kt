package com.gdh.shoppingmall.interceptor

import com.gdh.shoppingmall.domain.auth.UserContextHolder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

/**
 * 검증된 토큰으로부터 이메일을 가져와 UserContextHolder에 넣어주는 로직
 *
 * @Component -> 스프링이 관리하는 빈 클래스임을 나타낸다.
 * @Service와는 기술적으로 동일하지만 의미상으로 비즈니스 로직을 처리하는 클래스가 아니라는 차이가 있다.
 *
 * HandlerInterceptor을 상속받아 TokenValidationInterceptor 클래스를 생성하고 UserContextHolder를 사용하기 위해 생성자에서 주입받았다.
 *
 * 서버사이드 로깅을 위해 로거 객체를 프로퍼티로 선언
 */

@Component
class TokenValidationInterceptor @Autowired constructor(
    private val userContextHolder: UserContextHolder
) : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(this::class.java)
}