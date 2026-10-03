package com.bonfigli.storeapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val category: String,
    val description: String? = null,
    val image: String,
    val rating: Rating? = null
)

@Serializable
data class Rating(
    val rate: Double,
    val count: Int
)
