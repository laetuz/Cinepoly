package com.neotica.core.data.repository

import com.neotica.core.data.local.UserDao
import com.neotica.core.data.local.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.neotica.core.domain.repo.RegisterRepository

class RegisterRepositoryImpl(private val userDao: UserDao): RegisterRepository {
    override suspend fun register(username: String, email: String, pass: String, passConf: String){
        print("$username\n$email\n$pass\n$passConf")
        withContext(Dispatchers.IO){
            if (pass == passConf) {
                val userEntity = UserEntity(
                    username = username,
                    email = email,
                    password = pass,
                    token = "$username:$email"
                )
                val insertedId = userDao.insertUser(userEntity)
                if (insertedId != -1L) {
                    println("User registration successful")
                } else {
                    println("User registration failed")
                }
            } else {
                println("Password and password confirmation do not match")
            }
        }
    }
}