package com.gdh.shoppingmall.domain.product.registration

/**
 * 상품 등록에 필요한 데이터 클래스
 * 
 * 미리 등록된 이미지들의 id들을 리스트로 받기 위해 Long 타입의 리스트를 프로퍼티로 추가
 */

data class ProductRegistrationRequest(
    val name: String,
    val description: String,
    val price: Int,
    val categoryId: Int,
    val imageIds: List<Long?>
)
