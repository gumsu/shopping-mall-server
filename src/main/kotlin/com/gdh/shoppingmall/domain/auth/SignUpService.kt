package com.gdh.shoppingmall.domain.auth

import com.gdh.shoppingmall.domain.user.UserRepository
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
 */

@Service
class SignUpService @Autowired constructor(
    private val userRepository: UserRepository
) {
}