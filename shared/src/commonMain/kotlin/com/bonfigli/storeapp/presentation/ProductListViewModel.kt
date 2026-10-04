package com.bonfigli.storeapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonfigli.storeapp.data.FakeStoreHttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val httpClient: FakeStoreHttpClient = FakeStoreHttpClient()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading
            try {
                val products = httpClient.getProducts()
                if (products.isEmpty()) {
                    _uiState.value = ProductUiState.Error("No se encontraron productos disponibles en este momento.")
                } else {
                    _uiState.value = ProductUiState.Success(products)
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e::class.simpleName?.contains("Connect", ignoreCase = true) == true ||
                    e::class.simpleName?.contains("Host", ignoreCase = true) == true ||
                    e::class.simpleName?.contains("Socket", ignoreCase = true) == true ||
                    e::class.simpleName?.contains("IOException", ignoreCase = true) == true -> {
                        "Sin conexión a Internet. Por favor, verifica tu red y presiona Reintentar."
                    }
                    else -> "Error al conectar con la API: ${e.message ?: "Error desconocido"}"
                }
                _uiState.value = ProductUiState.Error(errorMessage)
            }
        }
    }
}
