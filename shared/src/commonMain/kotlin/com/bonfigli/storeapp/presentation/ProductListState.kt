package com.bonfigli.storeapp.presentation

import com.bonfigli.storeapp.domain.Product

sealed interface ProductUiState {
    data object Loading : ProductUiState
    data class Success(val products: List<Product>) : ProductUiState
    data class Error(val message: String) : ProductUiState
}
