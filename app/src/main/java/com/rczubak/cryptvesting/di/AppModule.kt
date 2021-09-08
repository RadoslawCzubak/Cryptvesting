package com.rczubak.cryptvesting.di

import android.content.Context
import androidx.room.Room
import com.rczubak.cryptvesting.BuildConfig
import com.rczubak.cryptvesting.data.database.cryptocurrencyinfo.CryptocurrenciesDao
import com.rczubak.cryptvesting.data.database.cryptocurrencyinfo.CryptocurrenciesDatabase
import com.rczubak.cryptvesting.data.database.transactions.TransactionsDao
import com.rczubak.cryptvesting.data.database.transactions.TransactionsDatabase
import com.rczubak.cryptvesting.data.remote.ApiKeyInterceptor
import com.rczubak.cryptvesting.data.remote.services.NomicsApi
import com.rczubak.cryptvesting.data.repository.MainRepositoryImpl
import com.rczubak.cryptvesting.domain.repository.MainRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideTransactionsDao(@ApplicationContext applicationContext: Context): TransactionsDao {
        val db = Room.databaseBuilder(
            applicationContext,
            TransactionsDatabase::class.java, "database-name"
        ).build()

        return db.transactionDao()
    }

    @Provides
    fun provideCryptoCurrenciesDao(@ApplicationContext context: Context): CryptocurrenciesDao {
        val db = Room.databaseBuilder(
            context,
            CryptocurrenciesDatabase::class.java, "cryptocurrencies-state-db",
        ).build()

        return db.dao()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(loggingInterceptor).build()
    }

    @Provides
    fun provideNomicsApi(client: OkHttpClient): NomicsApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.NOMICS_API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(NomicsApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindingModule {
    @Binds
    abstract fun bindMainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository
}