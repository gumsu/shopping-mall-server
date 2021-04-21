package com.gdh.shoppingmall.controller

import com.gdh.shoppingmall.common.ApiResponse
import com.gdh.shoppingmall.domain.auth.JWTUtil
import com.gdh.shoppingmall.domain.auth.SignInRequest
import com.gdh.shoppingmall.domain.auth.SignInService
import com.gdh.shoppingmall.domain.auth.UserContextHolder
import com.gdh.shoppingmall.interceptor.TokenValidationInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException

/**
 * signIn 함수를 API로 제공할 수 있도록 컨트롤러 생성
 * 
 * refreshToken -> 토큰 만료에 대비해 토큰 재발급 API
 *                 URL의 쿼리파라미터로 들어오는 grant_type값이 refresh_token이 아닌 경우 예외 처리
 *                 userContextHolder의 이메일을 가져와 토큰 재발급, 없을 경우 예외 처리
 */

@RestController
@RequestMapping("/api/v1")
class SignInApiController @Autowired constructor(
    private val signInService: SignInService,
    private val userContextHolder: UserContextHolder
) {
    @PostMapping("/signin")
    fun signIn(@RequestBody signInRequest: SignInRequest) =
        ApiResponse.ok(signInService.signIn(signInRequest))

    @PostMapping("/refresh_token")
    fun refreshToken(@RequestParam("grant_type") grantType: String) : ApiResponse {
        if(grantType != TokenValidationInterceptor.GRANT_TYPE_REFRESH) {
            throw IllegalArgumentException("grant_type 없음")
        }

        return userContextHolder.email?.let { 
            ApiResponse.ok(JWTUtil.createToken(it))
        } ?: throw IllegalArgumentException("사용자 정보 없음")
    }
}