package com.example.dora.network.api

import com.example.dora.network.NetworkRequest
import com.example.dora.network.NetworkResponse

interface RemoteDatabaseAPI<O, E : Throwable> {

    fun <I> insert(toInsert: NetworkRequest<I>) : NetworkResponse<O, E>

    fun <I> update(toUpdate: NetworkRequest<I>) : NetworkResponse<O, E>

    fun <I> delete(toDelete: NetworkRequest<I>) : NetworkResponse<O, E>

    fun <I> findOne(predicate: NetworkRequest<I>) : NetworkResponse<O, E>

    fun <I> findMany(predicate: NetworkRequest<I>) : NetworkResponse<O, E>

}