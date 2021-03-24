package com.gdh.shoppingmall.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository 애노테이션은 이 인터페이스가 스프링이 관리하는 레포지토리 빈으로서 동작한다는 것을 나타낸다.
 * 레포지토리는 의미적으로 이 클래스가 데이터의 읽기/쓰기 등을 담당한다는 것을 표시
 *
 * JpaRepository 상속을 받으면 레포지토리를 JPA 스펙에 맞게 확장하면서 기본적인 CRUD 함수를 제공하게 한다.
 *
 * 이메일 중복 검사를 하는 checkDuplicates() 함수를 구현해야 하는데,
 * 이메일로 회원 정보를 검색할 수 있는 기능이 필요하다.
 * Spring Data JPA에서는 레포지토리 인터페이스에 규칙에 맞는 간단한 함수를 정의하는 것으로 쿼리를 대신해주는 기능을 사용
 * findBy + 필드명 + And + 필드명 ...
 * 이메일 검색은 한 명 or 0명의 유저만 존재하므로 User? 반환
 */

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}