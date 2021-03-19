package com.gdh.shoppingmall.common

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController

/**
 * ParayoException과 그 외에 모든 예외들을 캐치해 메시지를 전파
 */

@Controller
@RestController
class ParayoExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(ParayoException::class)
    fun handleParayoException(e: ParayoException): ApiResponse {
        logger.error("API Error", e)
        return ApiResponse.error(e.message)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ApiResponse {
        logger.error("API Error", e)
        return ApiResponse.error("알 수 없는 오류가 발생했습니다.")
    }
}