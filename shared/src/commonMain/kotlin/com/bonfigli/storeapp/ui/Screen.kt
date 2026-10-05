package com.bonfigli.storeapp.ui

import com.bonfigli.storeapp.domain.Product

sealed interface Screen {
    data object ProductList : Screen
    data class ProductDetail(val product: Product) : Screen
    data object Cart : Screen
}
