package com.gdh.shoppingmall.domain.product

/**
 * 상품 리스트에 표시될 데이터 클래스 정의
 *
 * Product. -> 확장함수를 이용해 Product 객체를 toProductListItemResponse 로 변환하는 함수를 제공
 * id?.let -> 레포지토리로부터 정상적으로 데이터를 읽어왔다면 Product의 id가 null인 경우는 발생하지 않겠지만,
 *            방어를 위해 id가 null인 경우 정상적이지 않은 데이터이므로 null을 반환
 * toThumbs() -> 확장함수를 정의해 ProductImage를 썸네일 주소로 변경해준다.
 * 확장자가 jpg가 아닐 경우 jpg로 변경해준다.
 */

data class ProductListItemResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val status: String,
    val sellerId: Long,
    val imagePaths: List<String>
)

fun Product.toProductListItemResponse() = id?.let {
    ProductListItemResponse(it, name, description, price, status.name, userId, images.map { it.toThumbs() })
}

fun ProductImage.toThumbs(): String {
    val ext = path.takeLastWhile { it != '.' }
    val fileName = path.takeWhile { it != '.' }
    val thumbnailPath = "$fileName-thumb.$ext"

    return if (ext == "jpg") thumbnailPath else "$thumbnailPath.jpg"
}