package com.gdh.shoppingmall.domain.auth

/**
 * 회원 가입에 필요한 파라미터로 쓰이는 데이터 클래스
 */

data class SignUpRequest(
    val email: String,
    val name: String,
    val password: String
)