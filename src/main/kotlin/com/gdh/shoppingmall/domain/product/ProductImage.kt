package com.gdh.shoppingmall.domain.product

import com.gdh.shoppingmall.domain.jpa.BaseEntity
import java.util.*
import javax.persistence.*

@Entity(name = "product_image")
class ProductImage(var path: String, var productId: Long? = null) : BaseEntity() {

}