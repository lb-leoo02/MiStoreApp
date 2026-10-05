package com.bonfigli.storeapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bonfigli.storeapp.presentation.CartViewModel
import com.bonfigli.storeapp.presentation.ProductListViewModel
import com.bonfigli.storeapp.presentation.ProductUiState
import com.bonfigli.storeapp.ui.BackHandler
import com.bonfigli.storeapp.ui.CartScreen
import com.bonfigli.storeapp.ui.ProductDetailScreen
import com.bonfigli.storeapp.ui.ProductListScreen
import com.bonfigli.storeapp.ui.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    productListViewModel: ProductListViewModel = viewModel { ProductListViewModel() },
    cartViewModel: CartViewModel = viewModel { CartViewModel() }
) {
    val uiState by productListViewModel.uiState.collectAsStateWithLifecycle()
    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()
    var currentScreen by remember { mutableStateOf<Screen>(Screen.ProductList) }

    val totalCartCount = cartItems.sumOf { it.quantity }

    // Manejador del botón físico de regresar (Back gesture/button)
    if (currentScreen is Screen.ProductDetail || currentScreen is Screen.Cart) {
        BackHandler {
            currentScreen = Screen.ProductList
        }
    }

    MaterialTheme {
        when (val screen = currentScreen) {
            is Screen.ProductList -> {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Mi Store App", fontWeight = FontWeight.Bold) },
                            actions = {
                                BadgedBox(
                                    badge = {
                                        if (totalCartCount > 0) {
                                            Badge {
                                                Text(if (totalCartCount > 99) "99+" else totalCartCount.toString())
                                            }
                                        }
                                    },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    IconButton(onClick = { currentScreen = Screen.Cart }) {
                                        Text("🛒", style = MaterialTheme.typography.titleMedium)
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (val state = uiState) {
                            is ProductUiState.Loading -> {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator()
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("Cargando catálogo de productos...")
                                }
                            }

                            is ProductUiState.Success -> {
                                ProductListScreen(
                                    products = state.products,
                                    onProductClick = { product ->
                                        currentScreen = Screen.ProductDetail(product)
                                    }
                                )
                            }

                            is ProductUiState.Error -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "¡Ups! Ocurrió un problema",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = state.message,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(onClick = { productListViewModel.loadProducts() }) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is Screen.ProductDetail -> {
                ProductDetailScreen(
                    product = screen.product,
                    onAddToCart = { product ->
                        cartViewModel.addProduct(product)
                    },
                    onBackClick = {
                        currentScreen = Screen.ProductList
                    }
                )
            }

            is Screen.Cart -> {
                CartScreen(
                    cartItems = cartItems,
                    onIncrementQuantity = { id -> cartViewModel.incrementQuantity(id) },
                    onDecrementQuantity = { id -> cartViewModel.decrementQuantity(id) },
                    onRemoveItem = { id -> cartViewModel.removeItem(id) },
                    onClearCart = { cartViewModel.clearCart() },
                    onBackClick = { currentScreen = Screen.ProductList },
                    onExploreClick = { currentScreen = Screen.ProductList }
                )
            }
        }
    }
}
