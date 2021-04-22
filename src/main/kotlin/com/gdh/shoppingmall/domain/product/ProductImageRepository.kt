package com.gdh.shoppingmall.domain.product

import org.springframework.data.jpa.repository.JpaRepository

/**
 * findByIdIn() -> 데이터베이스로부터 이미지 정보를 읽어오기 위해 추가
 */

interface ProductImageRepository: JpaRepository<ProductImage, Long> {
    fun findByIdIn(imageIds: List<Long>): MutableList<ProductImage>
}