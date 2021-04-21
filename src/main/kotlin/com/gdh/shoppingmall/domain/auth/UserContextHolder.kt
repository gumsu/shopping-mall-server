package com.gdh.shoppingmall.domain.auth

import com.gdh.shoppingmall.domain.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 스프링 MVC의 요청은 대개 한 스레드 내에서 처리되므로 ThreadLocal을 이용해 사용자 정보를 저장할 수 있다.
 * 요청이 끝나는 시점에 이 변수를 초기화해야 다른 사용자의 요청이 동일 스레드 내에서 실행되었을 때,
 * 사용자 정보가 섞이지 않을 수 있고, 특정 요청을 멀티쓰레드로 처리하려는 경우 새 스레드에서는 사용자 정보에 접근할 수 없다.
 *
 * 이 클래스는 UserRepository로부터 사용자 정보를 읽어 ThreadLocal<UserHolder> 타입의 프로퍼티에 사용자 정보를 저장해주는
 * set() 함수와 초기화시켜주는 clear() 함수를 가지고 있다.
 *
 * 사용자 정보 중 사용할 정보 id, email, name 프로퍼티를 선언
 */

@Service
class UserContextHolder @Autowired constructor(private val userRepository: UserRepository) {
    private val userHolder = ThreadLocal.withInitial {
        UserHolder()
    }

    val id: Long? get() = userHolder.get().id
    val name: String? get() = userHolder.get().name
    val email: String? get() = userHolder.get().email

    fun set(email: String) = userRepository.findByEmail(email)?.let { user ->
        this.userHolder.get().apply {
            this.id = user.id
            this.name = user.name
            this.email = user.email
        }.run(userHolder::set)
    }

    fun clear() {
        userHolder.remove()
    }

    class UserHolder {
        var id: Long? = null
        var email: String? = null
        var name: String? = null
    }
}