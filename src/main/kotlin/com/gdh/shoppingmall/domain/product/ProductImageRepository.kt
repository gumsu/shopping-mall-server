package com.gdh.shoppingmall.domain.product

import org.springframework.data.jpa.repository.JpaRepository

interface ProductImageRepository: JpaRepository<ProductImage, Long> {
}