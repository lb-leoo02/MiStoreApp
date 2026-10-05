package com.bonfigli.storeapp

import com.bonfigli.storeapp.domain.Product
import com.bonfigli.storeapp.presentation.CartViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CartTest {

    private fun createSampleProduct(id: Int, title: String, price: Double): Product {
        return Product(
            id = id,
            title = title,
            price = price,
            category = "Electronics",
            description = "Sample product description"
        )
    }

    @Test
    fun testAddProductToCart() {
        val viewModel = CartViewModel()
        val product1 = createSampleProduct(id = 1, title = "Laptop", price = 999.99)

        // Inicialmente el carrito debe estar vacío
        assertEquals(0, viewModel.cartItems.value.size)
        assertEquals(0, viewModel.getTotalItemCount())
        assertEquals(0.0, viewModel.getTotalPrice())

        // Agregar un producto
        viewModel.addProduct(product1)

        assertEquals(1, viewModel.cartItems.value.size)
        assertEquals(1, viewModel.getTotalItemCount())
        assertEquals(999.99, viewModel.getTotalPrice())

        // Agregar el mismo producto incrementa la cantidad
        viewModel.addProduct(product1)

        assertEquals(1, viewModel.cartItems.value.size)
        assertEquals(2, viewModel.cartItems.value.first().quantity)
        assertEquals(2, viewModel.getTotalItemCount())
        assertEquals(1999.98, viewModel.getTotalPrice())
    }

    @Test
    fun testIncrementAndDecrementQuantity() {
        val viewModel = CartViewModel()
        val product = createSampleProduct(id = 1, title = "Auriculares", price = 50.0)

        viewModel.addProduct(product)
        assertEquals(1, viewModel.cartItems.value.first().quantity)

        // Incrementar
        viewModel.incrementQuantity(productId = 1)
        assertEquals(2, viewModel.cartItems.value.first().quantity)
        assertEquals(100.0, viewModel.getTotalPrice())

        // Decrementar
        viewModel.decrementQuantity(productId = 1)
        assertEquals(1, viewModel.cartItems.value.first().quantity)
        assertEquals(50.0, viewModel.getTotalPrice())

        // Decrementar cuando la cantidad es 1 elimina el producto del carrito
        viewModel.decrementQuantity(productId = 1)
        assertTrue(viewModel.cartItems.value.isEmpty())
        assertEquals(0, viewModel.getTotalItemCount())
        assertEquals(0.0, viewModel.getTotalPrice())
    }

    @Test
    fun testRemoveItem() {
        val viewModel = CartViewModel()
        val p1 = createSampleProduct(id = 1, title = "Teclado", price = 80.0)
        val p2 = createSampleProduct(id = 2, title = "Mouse", price = 40.0)

        viewModel.addProduct(p1)
        viewModel.addProduct(p2)

        assertEquals(2, viewModel.cartItems.value.size)
        assertEquals(120.0, viewModel.getTotalPrice())

        // Eliminar p1
        viewModel.removeItem(productId = 1)

        assertEquals(1, viewModel.cartItems.value.size)
        assertEquals(2, viewModel.cartItems.value.first().product.id)
        assertEquals(40.0, viewModel.getTotalPrice())
    }

    @Test
    fun testClearCart() {
        val viewModel = CartViewModel()
        viewModel.addProduct(createSampleProduct(id = 1, title = "Monitor", price = 300.0))
        viewModel.addProduct(createSampleProduct(id = 2, title = "Silla", price = 150.0))

        assertEquals(2, viewModel.cartItems.value.size)
        assertEquals(450.0, viewModel.getTotalPrice())

        viewModel.clearCart()

        assertTrue(viewModel.cartItems.value.isEmpty())
        assertEquals(0, viewModel.getTotalItemCount())
        assertEquals(0.0, viewModel.getTotalPrice())
    }
}
