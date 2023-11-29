package com.neotica.core.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("select * from user_table where email = :email and password = :password limit 1")
    suspend fun getUser(email: String, password: String) : UserEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: UserEntity) : Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateUser(user: UserEntity)

    @Query("select * from user_table where userId like:userId")
    fun getId(userId: String) : Int

    @Query("SELECT * from user_table WHERE token = :token")
    fun getUsername(token : String): LiveData<UserEntity>
}