package com.example.dora.network.api

import com.example.dora.network.NetworkRequest
import com.example.dora.network.NetworkResponse

interface StorageAPI<I, O, E : Throwable> {
    fun uploadFile(file: NetworkRequest<I>) : NetworkResponse<O, E>
}