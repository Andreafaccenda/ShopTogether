package com.example.speedmarket.di

import android.app.Application
import android.content.SharedPreferences
import com.example.speedmarket.repository.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth,
        appPreferences: SharedPreferences,
        gson: Gson
    ): AuthRepository {
        return AuthRepositoryImp(auth,database,appPreferences,gson)
    }

    @Provides
    @Singleton
    fun provideProdRepository(
        database: FirebaseFirestore,
        application: Application
    ): ProdRepository {
        return ProdRepositoryImp(database, application)
    }

    @Provides
    @Singleton
    fun provideCarrelloRepository(
        database: FirebaseFirestore,
        application: Application
    ): CarrelloRepository {
        return CarrelloRepositoryImp(database, application)
    }
}