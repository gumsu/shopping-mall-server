package com.gdh.shoppingmall.domain.product.registration

import com.gdh.shoppingmall.common.ParayoException
import com.gdh.shoppingmall.domain.auth.UserContextHolder
import com.gdh.shoppingmall.domain.product.*
import org.springframework.beans.factory.annotation.Autowired

/**
 * 상품 등록 로직
 *
 * register() -> ProductRegistrationRequest를 파라미터로 받는 함수, 사용자 아이디가 존재하는 지 검사
 *               image를 laze 델리게이트로 선언한 이유는 val 키워드를 상단에 위치시켰을 때 불필요한 데이터베이스 접근을 막기 위함
 *
 * validateRequest() -> 요청 객체의 유효성 검증
 * toProduct() -> 엔티티로 변환
 * 코틀린은 전역적으로 사용될만한 확장 함수는 해당 클래스가 선언된 파일에 확장 함수를 함께 정의하고,
 * 특별한 클래스에서만 사용되는 확장 함수는 이 함수를 사용할 클래스 정의 아래에 정의하는 것을 권장한다.
 */

class ProductRegistrationService @Autowired constructor(
    private val productRepository: ProductRepository,
    private val productImageRepository: ProductImageRepository,
    private val userContextHolder: UserContextHolder
) {
    fun register(request: ProductRegistrationRequest) =
        userContextHolder.id?.let { userId ->
            val images by lazy { findAndValidateImages(request.imageIds) }
            request.validateRequest()
            request.toProduct(images, userId)
                .run(::save)
        } ?: throw ParayoException("상품 등록에 필요한 사용자 정보가 존재하지 않습니다.")

    private fun findAndValidateImages(imageIds: List<Long?>) =
        productImageRepository.findByIdIn(imageIds.filterNotNull())
            .also { images ->
                images.forEach { image ->
                    if (image.productId != null)
                        throw ParayoException("이미 등록된 상품입니다.")
                }
            }

    private fun save(product: Product) = productRepository.save(product)

    private fun ProductRegistrationRequest.validateRequest() = when {
        name.length !in 1..40 ||
                imageIds.size !in 1..4 ||
                imageIds.filterNotNull().isEmpty() ||
                description.length !in 1..500 ||
                price <= 0 ->
            throw ParayoException("올바르지 않은 상품 정보입니다.")
        else -> {
        }
    }

    private fun ProductRegistrationRequest.toProduct(images: MutableList<ProductImage>, userId: Long) =
        Product(name, description, price, categoryId, ProductStatus.SELLABLE, images, userId)
}