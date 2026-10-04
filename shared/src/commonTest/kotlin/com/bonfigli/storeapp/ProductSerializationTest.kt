package com.bonfigli.storeapp

import com.bonfigli.storeapp.domain.ProductResponse
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ProductSerializationTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Test
    fun testProductResponseDeserialization() {
        val sampleJson = """
            {
                "data": [
                    {
                        "_id": 1,
                        "title": "Remera de Algodón",
                        "isNew": true,
                        "oldPrice": "30.00",
                        "price": 24.99,
                        "description": "Una remera cómoda de algodón 100%",
                        "category": "Ropa",
                        "brand": "AranguriBrand"
                    }
                ],
                "totalProducts": 1,
                "totalPages": 1,
                "currentPage": 1,
                "perPage": 10
            }
        """.trimIndent()

        val response = json.decodeFromString<ProductResponse>(sampleJson)

        assertEquals(1, response.totalProducts)
        assertEquals(1, response.data.size)

        val product = response.data.first()
        assertEquals(1, product.id)
        assertEquals("Remera de Algodón", product.title)
        assertEquals(24.99, product.price)
        assertEquals("AranguriBrand", product.brand)
        assertTrue(product.isNew)
        assertNotNull(product.description)
    }
}
