package com.gdh.shoppingmall.domain.auth

import com.gdh.shoppingmall.common.ParayoException
import com.gdh.shoppingmall.domain.user.User
import com.gdh.shoppingmall.domain.user.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * signIn() -> 데이터베이스에 해당 이메일을 사용하는 유저가 존재하는지 검색한 후
 * 존재하지 않는다면 메시지와 Exception을 던진다. 이메일은 대소문자를 구분하지 않고
 * 모두 소문자로 치환한다.
 *
 * 이메일이 아닌 형식으로 가입되는 유저는 없기 때문에 이메일 형식 검증은 생략한다.
 * BCrypt.checkpw()로 입력한 비밀번호가 데이터베이스의 비밀번호 해시값과 일치하는지
 * 판별한 후 일치하는 경우에만 토큰을 생성해 반환
 */

@Service
class SignInService @Autowired constructor(
    private val userRepository: UserRepository
) {
    fun signIn(signInRequest: SignInRequest): SignInResponse {
        val user = userRepository
            .findByEmail(signInRequest.email.toLowerCase())
            ?: throw ParayoException("로그인 정보를 확인해주세요.")

        if (isNotValidPassword(signInRequest.password, user.password)) {
            throw ParayoException("로그인 정보를 확인해주세요.")
        }

        user.fcmToken = signInRequest.fcmToken
        userRepository.save(user)
        return responseWithTokens(user)
    }

    private fun isNotValidPassword(
        plain: String,
        hashed: String
    ) = BCrypt.checkpw(plain, hashed).not()

    private fun responseWithTokens(user: User) = user.id?.let { userId ->
        SignInResponse(
            JWTUtil.createToken(user.email),
            JWTUtil.createRefreshToken(user.email),
            user.name,
            userId
        )
    } ?: throw IllegalStateException("user.id 없음.")
}