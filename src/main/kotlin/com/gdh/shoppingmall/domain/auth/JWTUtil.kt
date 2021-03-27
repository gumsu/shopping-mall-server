package com.gdh.shoppingmall.domain.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

/**
 * 로그인 -> 토큰 발급하여 반환(JWT 라이브러리 사용)
 * 이 클래스는 하는 일이 고정적이고 다른 코드에 영향을 주지 않기 때문에 리소스 낭비를 줄이고자
 * 싱글톤으로 선언한다. 알고리즘과 표즌 클레임, 커스텀 클레임 몇 가지를 지정한다.
 *
 */

object JWTUtil {
    private const val ISSUER = "Parayo"
    private const val SUBJECT = "Auth"
    private const val EXPIRE_TIME = 60L * 60 * 2 * 1000 // 2시간

    private val SECRET = "your-secret"
    private val algorithm: Algorithm = Algorithm.HMAC256(SECRET)

    fun createToken(email: String) = JWT.create()
        .withIssuer(ISSUER)
        .withSubject(SUBJECT)
        .withIssuedAt(Date())
        .withExpiresAt(Date(Date().time + EXPIRE_TIME))
        .withClaim(JWTClaims.EMAIL, email)
        .sign(algorithm)

    object JWTClaims {
        const val EMAIL = "email"
    }
}