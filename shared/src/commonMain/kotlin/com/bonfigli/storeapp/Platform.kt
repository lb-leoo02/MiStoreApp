package com.bonfigli.storeapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform