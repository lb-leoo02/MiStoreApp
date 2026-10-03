package com.bonfigli.storeapp
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import mistoreapp.shared.generated.resources.Res
import mistoreapp.shared.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import com.bonfigli.storeapp.data.FakeStoreHttpClient
import com.bonfigli.storeapp.domain.Product
@Composable
@Preview
fun App() {
    MaterialTheme {
        var textoPantalla by remember { mutableStateOf("Cargando productos de la API...") }

        LaunchedEffect(Unit) {
            try {
                val cliente = FakeStoreHttpClient()
                val productos = cliente.getProducts()

                val primerProducto = productos.firstOrNull()
                textoPantalla = "¡Éxito! Se descargaron ${productos.size} productos.\n\n" +
                        "El primero es: ${primerProducto?.title ?: "Sin título"}\n" +
                        "Marca: ${primerProducto?.brand ?: "Sin marca"} | Precio: $${primerProducto?.price ?: 0.0}"
            } catch (e: Throwable) {
                textoPantalla = "Error fatal: ${e::class.simpleName}\nDetalle: ${e.message}"
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = textoPantalla)
        }
    }
}
