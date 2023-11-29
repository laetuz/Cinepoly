package com.neotica.profile

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotica.core.common.PreferenceDefaults
import com.neotica.core.data.local.UserEntity
import com.neotica.core.data.repository.PreferenceRepositoryImpl
import com.neotica.core.data.repository.ProfileRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import android.Manifest

class ProfileViewModel(
    private val repo: ProfileRepositoryImpl,
    private val prefRepo: PreferenceRepositoryImpl
) : ViewModel() {
    val token = prefRepo.getToken()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = PreferenceDefaults.TOKEN,
        )

    fun getUsername(userId: String): LiveData<UserEntity> = repo.getUsername(userId)

    fun updateUser(
        userId: Int,
        username: String,
        email: String,
        password: String,
        fullname: String? = null,
        dob: String? = null,
        address: String? = null,
        token: String
    ) {
        viewModelScope.launch {
            repo.updateUser(userId, username, email, password, fullname, dob, address, token)
        }
    }

    fun checkCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun logout(){
        viewModelScope.launch {
            viewModelScope.launch { repo.logout() }
        }
    }
}