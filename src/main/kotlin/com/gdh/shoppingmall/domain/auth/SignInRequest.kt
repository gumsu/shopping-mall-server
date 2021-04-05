package com.gdh.shoppingmall.domain.auth

/**
 * 로그인에 필요한 데이터: 이메일, 비밀번호
 */

data class SignInRequest(
    val email: String,
    val password: String
)