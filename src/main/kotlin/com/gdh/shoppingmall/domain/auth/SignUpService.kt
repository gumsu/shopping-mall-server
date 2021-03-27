package com.gdh.shoppingmall.domain.auth

import com.gdh.shoppingmall.common.ParayoException
import com.gdh.shoppingmall.domain.user.User
import com.gdh.shoppingmall.domain.user.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Service 애노테이션은 이 클래스가 스프링이 관리하는 빈 클래스임을 나타내며,
 * 그 중에서도 비즈니스 로직을 처리하는 클래스라는 것을 표시
 *
 * Autowired 애노테이션은 빈 클래스를 자동으로 주입받겠다는 것을 의미한다.
 * @Service와 같이 스프링의 빈으로 선언된 클래스와 생성자, setter, property 등에 붙이면
 * 스프링이 해당하는 빈을 알아서 주입한다.
 *
 * 사용자 데이터를 읽어 와야 하기 때문에 생성자에 데이터의 읽기/쓰기 등을 담당하는 UserRepository를 주입받는다.
 *
 * UserRepository가 상속받은 JpaRepository에는 이미 데이터를 저장하는 save() 함수가 구현되어 있다.
 * 여기에 User 객체를 전달하면 데이터베이스에 사용자 정보가 저장된다.
 * 비밀번호의 해시값과 회원 가입 시 요청된 데이터를 가지고 User 객체를 생성한 후
 * UserRepository를 통해 저장하면 회원 가입이 끝난다.
 */

@Service
class SignUpService @Autowired constructor(
    private val userRepository: UserRepository
) {
    fun signUp(signUpRequest: SignUpRequest) {
        validateRequest(signUpRequest)
        checkDuplicates(signUpRequest.email)
        registerUser(signUpRequest)
    }

    private fun validateRequest(signUpRequest: SignUpRequest) {
        validateEmail(signUpRequest.email)
        validateName(signUpRequest.name)
        validatePassword(signUpRequest.password)
    }

    private fun validateEmail(email: String) {
        val isNotValidEmail = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
            .toRegex(RegexOption.IGNORE_CASE)
            .matches(email)
            .not()

        if (isNotValidEmail) {
            throw ParayoException("이메일 형식이 올바르지 않습니다.")
        }
    }

    private fun validateName(name: String) {
        if (name.trim().length !in 2..20)
            throw ParayoException("이름은 2자 이상 20자 이하여야 합니다.")
    }

    private fun validatePassword(password: String) {
        if (password.trim().length !in 8..20)
            throw ParayoException("비밀번호는 공백을 제외하고 8자 이상 20자 이하여야 합니다.")
    }

    private fun checkDuplicates(email: String) = userRepository.findByEmail(email)?.let {
        throw ParayoException("이미 사용 중인 이메일입니다.")
    }

    private fun registerUser(signUpRequest: SignUpRequest) = with(signUpRequest) {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val user = User(email, hashedPassword, name)
        userRepository.save(user)
    }
}