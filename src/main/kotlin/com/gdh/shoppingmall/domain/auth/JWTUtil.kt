package com.gdh.shoppingmall.domain.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

/**
 * 로그인 -> 토큰 발급하여 반환(JWT 라이브러리 사용)
 * 이 클래스는 하는 일이 고정적이고 다른 코드에 영향을 주지 않기 때문에 리소스 낭비를 줄이고자
 * 싱글톤으로 선언한다. 알고리즘과 표즌 클레임, 커스텀 클레임 몇 가지를 지정한다.
 *
 * 표준 클레임 중 가장 중요한 것은 만료 시간이다. API용 토큰은 몇 시간 단위의 짧은 만료 시간을 가지고 있고,
 * 이 토큰이 만료되었을 때에는 리프레시용 토큰을 이용해 새로운 API 토큰을 발급 받아야 한다.
 * 리프레시 토큰이 만료되었을 때에는 로그인도 다시 해야하므로 리프레시 토큰의 만료 시간은 길게 설정한다.
 */

object JWTUtil {
    private const val ISSUER = "Parayo"
    private const val SUBJECT = "Auth"
    private const val EXPIRE_TIME = 60L * 60 * 2 * 1000 // 2시간
    private const val REFRESH_EXPIRE_TIME = 60L * 60 * 24 * 30 * 1000 //30일

    private val SECRET = "your-secret"
    private val algorithm: Algorithm = Algorithm.HMAC256(SECRET)

    private val refreshSecret = "your-refresh-secret"
    private val refreshAlgorithm: Algorithm = Algorithm.HMAC256(refreshSecret)

    fun createToken(email: String) = JWT.create()
        .withIssuer(ISSUER)
        .withSubject(SUBJECT)
        .withIssuedAt(Date())
        .withExpiresAt(Date(Date().time + EXPIRE_TIME))
        .withClaim(JWTClaims.EMAIL, email)
        .sign(algorithm)

    fun createRefreshToken(email: String) = JWT.create()
        .withIssuer(ISSUER)
        .withSubject(SUBJECT)
        .withIssuedAt(Date())
        .withExpiresAt(Date(Date().time + REFRESH_EXPIRE_TIME))
        .withClaim(JWTClaims.EMAIL, email)
        .sign(refreshAlgorithm)

    object JWTClaims {
        const val EMAIL = "email"
    }
}