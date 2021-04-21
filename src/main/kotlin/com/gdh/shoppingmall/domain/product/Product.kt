package com.gdh.shoppingmall.domain.product

import com.gdh.shoppingmall.domain.jpa.BaseEntity
import java.util.*
import javax.persistence.*

/**
 * @Enumerated -> enum 클래스가 데이터베이스에 어떤 형식으로 저장되어야 할 지를 지정
 * @OneToMany -> Product 하나에 ProductImage가 여러 개가 맵핑될 수 있다는 것을 나타내기 때문에
 *               image 프로퍼티는 MutableList로 선언
 * @JoinColumn -> 해당 엔티티의 PK와 맵핑되는 타겟 엔티티의 어떤 컬럼을 통해 두 테이블을 조인할 것인가?
 *                ProductImage에 productId라는 FK 생성하여 두 테이블을 조인
 */

@Entity(name = "product")
class Product(
    @Column(length = 40)
    var name: String,
    @Column(length = 500)
    var description: String,
    var price: Int,
    var categoryId: Int,
    @Enumerated(EnumType.STRING)
    var status: ProductStatus,
    @OneToMany
    @JoinColumn(name = "productId")
    var images: MutableList<ProductImage>,
    val userId: Long
) : BaseEntity() {

}