package com.bonfigli.storeapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import com.bonfigli.storeapp.presentation.ProductListViewModel
import com.bonfigli.storeapp.presentation.ProductUiState
import com.bonfigli.storeapp.ui.BackHandler
import com.bonfigli.storeapp.ui.ProductDetailScreen
import com.bonfigli.storeapp.ui.ProductListScreen
import com.bonfigli.storeapp.ui.Screen

@Composable
fun App(
    viewModel: ProductListViewModel = viewModel { ProductListViewModel() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentScreen by remember { mutableStateOf<Screen>(Screen.ProductList) }

    // Manejador del botón físico de regresar (Back gesture/button) de Android
    if (currentScreen is Screen.ProductDetail) {
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
                                    Button(onClick = { viewModel.loadProducts() }) {
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
                    onBackClick = {
                        currentScreen = Screen.ProductList
                    }
                )
            }
        }
    }
}
