package com.neotica.core.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user_table")
@Parcelize
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    @ColumnInfo(name = "username")
    val username: String?,
    @ColumnInfo(name = "email")
    val email: String?,
    @ColumnInfo(name = "password")
    val password: String?,
    @ColumnInfo(name = "fullname")
    val fullname: String? = null,
    @ColumnInfo(name = "dob")
    val dob: String? = null,
    @ColumnInfo(name = "address")
    val address: String? = null,
   // val profilePath: String? = null,
    val token: String = ""
) : Parcelable