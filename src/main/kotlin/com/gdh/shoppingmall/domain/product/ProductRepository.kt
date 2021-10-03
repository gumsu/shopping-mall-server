package com.gdh.shoppingmall.domain.product

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

/**
 * 상품 정보 읽어오는 레포지토리 - 상품을 카테고리별로, 스크롤에 다라 id 값을 기준으로 전/후를 읽어야 하므로
 *
 * findByCategoryIdAndIdGreaterThanOrderByIdDesc
 * 상품 리스트가 위쪽으로 스크롤될 때 호출되는 함수, 최신 데이터부터 읽음, 내림차순
 *
 * findByCategoryIdAndIdLessThanOrderByIdDesc
 * 상품 리스트가 아래쪽으로 스크롤될 때 호출되는 함수, 예전 데이터부터 읽음, 내림차순
 *
 * Pageable -> 검색 조건을 기준으로 페이지당 몇 개의 값을 읽을 것인지 설정
*/

interface ProductRepository: JpaRepository<Product, Long> {

    fun findByCategoryIdAndIdGreaterThanOrderByIdDesc(
        categoryId: Int?, id: Long, pageable: Pageable): List<Product>

    fun findByCategoryIdAndIdLessThanOrderByIdDesc(
        categoryId: Int?, id: Long, pageable: Pageable): List<Product>

    fun findByIdGreaterThanAndNameLikeOrderByIdDesc(
        id: Long, keyword: String, pageable: Pageable): List<Product>

    fun findByIdLessThanAndNameLikeOrderByIdDesc(
        id: Long, keyword: String, pageable: Pageable): List<Product>
}