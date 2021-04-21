package com.gdh.shoppingmall.domain.product

import java.util.*
import javax.persistence.*

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
    var image: MutableList<ProductImage>,
    val userId: Long
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var createdAt: Date? = null
    var updatedAt: Date? = null

    @PrePersist
    fun prePersist() {
        createdAt = Date()
        updatedAt = Date()
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = Date()
    }
}