package com.bonfigli.storeapp.data

import com.bonfigli.storeapp.domain.Product
import com.bonfigli.storeapp.domain.ProductResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class FakeStoreHttpClient(
    private val client: HttpClient = createHttpClient()
) {
    suspend fun getProducts(): List<Product> {
        return try {
            val response: ProductResponse = client.get("https://fakestoreapi.noksha.dev/api/products").body()
            response.data
        } catch (e: Exception) {
            throw e // Esto lanza el error hacia arriba para que App.kt lo pueda agarrar
        }
    }

    companion object {
        fun createHttpClient(): HttpClient {
            return HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    })
                }
            }
        }
    }
}
