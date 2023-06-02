package com.example.dora.network.api

import com.example.dora.network.NetworkRequest
import com.example.dora.network.NetworkResponse

interface RemoteDatabaseAPI<I, O, E : Throwable> {
  fun insert(request: NetworkRequest<I>): NetworkResponse<O, E>

  fun update(request: NetworkRequest<I>): NetworkResponse<O, E>

  fun delete(request: NetworkRequest<I>): NetworkResponse<O, E>

  fun findSingle(request: NetworkRequest<I>): NetworkResponse<O, E>

  fun findMany(request: NetworkRequest<I>): NetworkResponse<O, E>
}
