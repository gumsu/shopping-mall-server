package com.gdh.shoppingmall.domain.product

import com.gdh.shoppingmall.common.ParayoException

/**
 * 상품 정보를 내려주는 API
 */

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val status: String,
    val sellerId: Long,
    val imagePaths: List<String>
)

fun Product.toProductResponse() = id?.let {
    ProductResponse(it, name, description, price, status.name, userId, images.map { it.path })
} ?: throw ParayoException("상품 정보를 찾을 수 없습니다.")