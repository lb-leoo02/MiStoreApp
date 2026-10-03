package com.bonfigli.storeapp.data

import com.bonfigli.storeapp.domain.Product
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
            client.get("https://fakestoreapi.com/products").body()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
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
