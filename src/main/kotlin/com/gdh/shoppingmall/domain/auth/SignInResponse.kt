package com.gdh.shoppingmall.domain.auth

/**
 * 응답에 필요한 데이터: 토큰, 리프레시 토큰, 사용자 이름, 아이디
 */

data class SignInResponse(
    val token: String,
    val refreshToken: String,
    val userName: String,
    val userId: Long
)
