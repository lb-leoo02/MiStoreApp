package com.bonfigli.storeapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    @SerialName("data")
    val data: List<Product> = emptyList(),
    @SerialName("totalProducts")
    val totalProducts: Int = 0,
    @SerialName("totalPages")
    val totalPages: Int = 0,
    @SerialName("currentPage")
    val currentPage: Int = 0,
    @SerialName("perPage")
    val perPage: Int = 0
)

@Serializable
data class Product(
    @SerialName("_id")
    val id: Int,
    val title: String,
    val isNew: Boolean = false,
    val oldPrice: String? = null,
    val price: Double,
    val discountedPrice: Double? = null,
    val description: String? = null,
    val category: String? = null,
    val type: String? = null,
    val stock: Int? = null,
    val brand: String? = null,
    val size: List<String> = emptyList(),
    val image: String? = null,
    val rating: Int? = null
)
