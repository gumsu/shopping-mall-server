package com.gdh.shoppingmall.interceptor

import com.gdh.shoppingmall.domain.auth.JWTUtil
import com.gdh.shoppingmall.domain.auth.UserContextHolder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 검증된 토큰으로부터 이메일을 가져와 UserContextHolder에 넣어주는 로직
 *
 * @Component -> 스프링이 관리하는 빈 클래스임을 나타낸다.
 * @Service와는 기술적으로 동일하지만 의미상으로 비즈니스 로직을 처리하는 클래스가 아니라는 차이가 있다.
 *
 * HandlerInterceptor을 상속받아 TokenValidationInterceptor 클래스를 생성하고 UserContextHolder를 사용하기 위해 생성자에서 주입받았다.
 *
 * 서버사이드 로깅을 위해 로거 객체를 프로퍼티로 선언
 *
 * preHandle() -> 요청 처리 전에 호출, 토큰과 URL을 검증하고 미리 사용자 정보를 셋팅해주는 로직
 * postHandle() -> 요청 처리 후 뷰가 렌더링 되기 전에 호출, 스레드에 사용한 사용자 정보를 초기화해주는 로직
 *
 * request.getHeader(AUTHORIZATION)로 HttpServletRequest에 포함된 Authorization 헤더를 반환하여
 * 헤더 값이 없는 경우 허용된 URL인지 검사하여, 값이 있는 경우 토큰을 추출해 유효성을 검증하고 사용자 정보를 셋팅
 * 값이 없거나 허용되지 않은 URL이라면 401 에러 반환하여 클라이언트에 권한이 없음을 알린다. 로그인이나 토큰 갱신이 필요
 *
 * handleToken() -> JWTUtil의 함수를 이용해 토큰을 검증,
 *                  검증된 토큰에 있는 email 클레임을 이용해 UserContextHolder에 사용자 정보를 설정
 */

@Component
class TokenValidationInterceptor @Autowired constructor(
    private val userContextHolder: UserContextHolder
) : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authHeader = request.getHeader(AUTHORIZATION)

        if (authHeader.isNullOrBlank()) {
            val pair = request.method to request.servletPath
            if(!DEFAULT_ALLOWED_API_URLS.contains(pair)) {
                response.sendError(401)
                return false
            }
            return true
        } else {
            val grantType = request.getParameter(GRANT_TYPE)
            val token = extractToken(authHeader)
            return handleToken(grantType, token, response)
        }
    }

    private fun handleToken(grantType: String?, token: String, response: HttpServletResponse) = try {
        val jwt = when(grantType) {
            GRANT_TYPE_REFRESH -> JWTUtil.verifyRefresh(token)
            else -> JWTUtil.verify(token)
        }
        val email = JWTUtil.extractEmail(jwt)
        userContextHolder.set(email)
        true
    } catch (e: Exception) {
        logger.error("토큰 검증 실패. token = $token", e)
        response.sendError(401)
        false
    }

    private fun extractToken(token: String) = token.replace(BEARER, "").trim()

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        userContextHolder.clear()
    }

    /*
     * 토큰 검증에 사용될 상수
     * AUTHORIZATION: Authorization 토큰이 포함된 헤더 값을 가져오기 위한 상수
     * BEARER: Authorization 헤더에 JWT 토큰을 전달할 때 사용되는 인증 방법을 나타내는 스키마,
     *         실제 사용하려면 헤더 값에서 이 문자열을 제거 후 사용
     * GRANT_TYPE, GRANT_TYPE_REFRESH: 토큰 재발행을 요청할 때 사용하는 파라미터의 키와 값
     * DEFAULT_ALLOWED_API_URLS: 토큰 인증 없이 사용할 수 있는 URL 정의하기 위해 선언한 리스트(Spring Security 사용 가능)
     */
    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer"
        private const val GRANT_TYPE = "grant_type"
        const val GRANT_TYPE_REFRESH = "refresh_token"

        private val DEFAULT_ALLOWED_API_URLS = listOf(
            "POST" to "/api/v1/signin",
            "POST" to "/api/v1/users")
    }
}