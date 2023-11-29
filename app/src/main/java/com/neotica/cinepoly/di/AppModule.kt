package com.neotica.cinepoly.di

import com.neotica.cinepoly.ui.login.LoginViewModel
import com.neotica.cinepoly.ui.register.RegisterViewModel
import com.neotica.core.di.appModules
import com.neotica.core.di.networkModule
import com.neotica.core.di.remoteDataSourceModule
import com.neotica.core.di.repoModules
import com.neotica.core.di.userDatabaseModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModules = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}


val modules = listOf(
    appModules,
    viewModelModules,
    repoModules,
    networkModule,
    remoteDataSourceModule,
    userDatabaseModule,
)