package com.neotica.core.domain.repo

interface RegisterRepository {
    suspend fun register(username: String, email: String, pass: String, passConf: String)
}