package com.neotica.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.neotica.core.common.SharedPrefUtil
import com.neotica.core.data.local.UserDatabase
import com.neotica.core.data.remote.ApiService
import com.neotica.core.data.remote.RemoteDataSource
import com.neotica.core.data.repository.LoginRepositoryImpl
import com.neotica.core.data.repository.MainRepositoryImpl
import com.neotica.core.data.repository.PreferenceRepositoryImpl
import com.neotica.core.data.repository.ProfileRepositoryImpl
import com.neotica.core.data.repository.RegisterRepositoryImpl
import com.neotica.core.domain.repo.LoginRepository
import com.neotica.core.domain.repo.MainRepository
import com.neotica.core.domain.repo.PreferenceRepository
import com.neotica.core.domain.repo.ProfileRepository
import com.neotica.core.domain.repo.RegisterRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModules = module {
    singleOf(::provideDataStore) // Provide the DataStore, where provideDataStore is a function that sets up DataStore
    single { SharedPrefUtil(get()) } // Provide the SharedPrefUtil
}

val repoModules = module {
    singleOf(::PreferenceRepositoryImpl) bind PreferenceRepository::class
    singleOf(::MainRepositoryImpl) bind MainRepository::class
    singleOf(::LoginRepositoryImpl) bind LoginRepository::class
    singleOf(::RegisterRepositoryImpl) bind RegisterRepository::class
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class
}

val networkModule = module {
    single {
        val logBody = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(logBody)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            //.certificatePinner(certificatePinner)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val userDatabaseModule = module {
    factory { get<UserDatabase>().UserDao() }
    single {
        Room.databaseBuilder(
            androidContext(), UserDatabase::class.java, "User.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}

val remoteDataSourceModule = module {
    factory { RemoteDataSource(get()) }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preference_state")

fun provideDataStore(context: Context): DataStore<Preferences> {
    return context.dataStore
}