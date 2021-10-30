package com.gdh.shoppingmall.controller

import com.gdh.shoppingmall.common.ApiResponse
import com.gdh.shoppingmall.domain.auth.SignUpRequest
import com.gdh.shoppingmall.domain.auth.SignUpService
import com.gdh.shoppingmall.domain.auth.UserContextHolder
import com.gdh.shoppingmall.domain.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * SignUpService를 주입받아 signUp() 함수를 호출하는 API
 *
 * RequestBody 애노테이션은 데이터를 HTTP의 바디에서 읽는다는 것을 의미한다.
 * ?password=test 처럼 URI에 따라 붙는 쿼리스트링은
 * 웹서버의 로그에 그대로 저장되거나 웹브라우저의 캐시에 저장될 수 있으므로 보안상 치명적이다.
 * 이를 피하기 위해서 데이터를 바디에 담고 HTTPS를 이용해 통신하여 기본 보안 문제를 피해갈 수 있다.
 * 
 * 토큰이 변경되었을 때 로그인 사용자의 토큰을 업데이트할 API 추가
 */

@RestController
@RequestMapping("/api/v1")
class UserApiController @Autowired constructor(
    private val signUpService: SignUpService,
    private val userService: UserService,
    private val userContextHolder: UserContextHolder
) {
    @PostMapping("/users")
    fun signUp(@RequestBody signUpRequest: SignUpRequest) = ApiResponse.ok(signUpService.signUp(signUpRequest))

    @PostMapping("/users/fcm-token")
    fun updateFcmToken(@RequestBody fcmToken: String) = userContextHolder.id?.let { userId ->
        ApiResponse.ok(userService.updateFcmToken(userId, fcmToken))
    } ?: ApiResponse.error("토큰 갱신 실패")
}