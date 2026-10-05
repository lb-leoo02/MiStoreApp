package com.bonfigli.storeapp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bonfigli.storeapp.domain.Product
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    onAddToCart: (Product) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle del Producto",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFF8FAFC)
                    )
                },
                navigationIcon = {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF262C3D).copy(alpha = 0.7f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.18f)),
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(40.dp)
                            .clickable(onClick = onBackClick)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "←",
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF11141E).copy(alpha = 0.75f),
                    titleContentColor = Color(0xFFF8FAFC)
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF11141E).copy(alpha = 0.85f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            onAddToCart(product)
                            scope.launch {
                                snackbarHostState.showSnackbar("¡${product.title} agregado al carrito!")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Agregar al Carrito — $${product.price}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF212635).copy(alpha = 0.75f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.18f)),
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (!product.image.isNullOrEmpty()) {
                        AsyncImage(
                            model = product.image,
                            contentDescription = product.title,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Text(
                            text = "Sin Imagen disponible",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            product.brand?.let { brand ->
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF1E293B).copy(alpha = 0.8f),
                    border = BorderStroke(1.dp, Color(0xFF94A3B8).copy(alpha = 0.3f))
                ) {
                    Text(
                        text = brand.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFFCBD5E1),
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = product.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF8FAFC)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF60A5FA),
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            product.description?.let { desc ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A1F2C).copy(alpha = 0.75f)
                    ),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Descripción del producto",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF8FAFC)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            }
        }
    }
}
