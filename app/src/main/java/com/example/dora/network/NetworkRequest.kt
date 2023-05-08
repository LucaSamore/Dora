package com.example.dora.network

data class NetworkRequest<I>(val body: I) {
    companion object {
        fun <I> of(body: I): NetworkRequest<I> = NetworkRequest(body)
    }
}
