package com.rczubak.cryptvesting.di.modules

import android.content.Context
import androidx.room.Room
import com.rczubak.cryptvesting.BuildConfig
import com.rczubak.cryptvesting.data.dao.cryptocurrencyinfo.CryptocurrenciesDao
import com.rczubak.cryptvesting.data.dao.cryptocurrencyinfo.CryptocurrenciesDatabase
import com.rczubak.cryptvesting.data.dao.transactions.TransactionsDao
import com.rczubak.cryptvesting.data.dao.transactions.TransactionsDatabase
import com.rczubak.cryptvesting.data.network.ApiKeyInterceptor
import com.rczubak.cryptvesting.data.network.services.NomicsApi
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
final class AppModule {
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