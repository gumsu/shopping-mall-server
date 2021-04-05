package com.gdh.shoppingmall.controller

import com.gdh.shoppingmall.common.ApiResponse
import com.gdh.shoppingmall.domain.auth.SignInRequest
import com.gdh.shoppingmall.domain.auth.SignInService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * signIn 함수를 API로 제공할 수 있도록 컨트롤러 생성
 */

@RestController
@RequestMapping("/api/v1")
class SignInApiController @Autowired constructor(
    private val signInService: SignInService
) {
    @PostMapping("/signin")
    fun signIn(@RequestBody signInRequest: SignInRequest) =
        ApiResponse.ok(signInService.signIn(signInRequest))
}