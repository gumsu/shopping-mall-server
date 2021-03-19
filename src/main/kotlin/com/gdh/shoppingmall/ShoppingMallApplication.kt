package com.gdh.shoppingmall

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * 자신이 스프링 설정 클래스임을 나타냄과 동시에
 * 스프링 부트 애플리케이션이 실행될 때
 * 패키지 하위의 스프링 컴포넌트들을 재귀적으로 탐색해 등록 가능하도록 만든다.
 */

@SpringBootApplication
class ShoppingMallApplication

fun main(args: Array<String>) {
    runApplication<ShoppingMallApplication>(*args)
}
