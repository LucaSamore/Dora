package com.example.dora.network

data class NetworkResponse<O, E : Throwable>(val data: O?, val error: E?)