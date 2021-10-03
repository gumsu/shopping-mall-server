package com.gdh.shoppingmall.domain.product

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * productRepository의 쿼리 기반으로 상품 리스트를 읽어오는 로직
 * 검색 조건으로 받을 값들은 카테고리 id, 상품의 id, 검색 방향(전/후), 읽어올 개수
 *
 * PageRequest.of(0, limit) -> 각 조건에 맞는 0페이지를 limit 수만큼 가져오기 위한 Pageable을 상속받을 객체
 * ProductSearchCondition(categoryId != null, direction) -> 상품의 검색 조건을 표현하기 위한 객체
 *
 * get() -> 상품 하나를 조회하는 로직은 레포지토리에서 id를 읽어오는 것이다.
 *
 * ProductSearchCondition 에 hasKeyword 라는 이름으로 키워드 필드가 null이 아닌지 판단하는 필드를 추가 */

@Service
class ProductService @Autowired constructor(private val productRepository: ProductRepository) {

    fun search(categoryId: Int?, productId: Long, direction: String, keyword: String?, limit: Int): List<Product> {
        val pageable = PageRequest.of(0, limit)
        val condition = ProductSearchCondition(categoryId != null, direction, keyword != null)

        return when (condition) {
            NEXT_IN_SEARCH -> productRepository
                .findByIdLessThanAndNameLikeOrderByIdDesc(productId, "%$keyword%", pageable)
            PREV_IN_SEARCH -> productRepository
                .findByIdGreaterThanAndNameLikeOrderByIdDesc(productId, "%$keyword%", pageable)
            NEXT_IN_CATEGORY -> productRepository
                .findByCategoryIdAndIdLessThanOrderByIdDesc(categoryId, productId, pageable)
            PREV_IN_CATEGORY -> productRepository
                .findByCategoryIdAndIdGreaterThanOrderByIdDesc(categoryId, productId, pageable)
            else -> throw  IllegalArgumentException("상품 검색 조건 오류")
        }
    }

    fun get(id: Long) = productRepository.findByIdOrNull(id)

    data class ProductSearchCondition(val categoryIdIsNotNull: Boolean, val direction: String, val hasKeyword: Boolean = false)

    companion object {
        val NEXT_IN_SEARCH = ProductSearchCondition(false, "next", true)
        val PREV_IN_SEARCH = ProductSearchCondition(false, "prev", true)
        val NEXT_IN_CATEGORY = ProductSearchCondition(true, "next")
        val PREV_IN_CATEGORY = ProductSearchCondition(true, "next")
    }
}