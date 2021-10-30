package com.gdh.shoppingmall.domain.user

import com.gdh.shoppingmall.domain.jpa.BaseEntity
import java.util.*
import javax.persistence.*

/**
 * PrePersist -> DB에 새 데이터가 저장되기 전에 자동으로 호출된다.
 * 여기서는 데이터 저장 전에 가입일(데이터 생성일)을 현재 날짜로 지정해주도록 한다.
 * 이렇게 하면 새 데이터를 저장할 때 매번 user.createdAt = Date()와 같은 코드를 실행해야하는 번거로움이 사라지고
 * 날짜 누락시키는 실수를 방지할 수 있다.
 *
 * PreUpdate -> JPA 라이프사이클 훅을 지정한다.
 * DB에 데이터 업데이트 명령을 날리기 전에 실행된다.
 *
 * FCM 사용하면 유니크한 FCM 토큰이 생성
 * 한 유저에게 푸시알림을 보내기 위해서는 이 토큰을 사용자 정보와 함께 서버에 저장하고 사용
 * User 엔티티에 토큰 필드를 추가하고 가입 시 토큰을 함께 저장하고 로그인 시 토큰을 업데이트할 수 있도록 수정
 */

@Entity(name = "user")
class User(
    var email: String,
    var password: String,
    var name: String,
    var fcmToken: String?
) : BaseEntity(){
}