package com.nekhaev.android.smartled.di

import com.nekhaev.android.smartled.data.remote.SmartLedApi
import com.nekhaev.android.smartled.data.remote.SmartLedApiImpl
import com.nekhaev.android.smartled.data.repository.SmartLedRepositoryImpl
import com.nekhaev.android.smartled.data.utils.WifiScanUtilImpl
import com.nekhaev.android.smartled.domain.repository.SmartLedRepository
import com.nekhaev.android.smartled.domain.utils.WifiScanUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideWifiScanUtil(): WifiScanUtil {
        return WifiScanUtilImpl()
    }

    @Provides
    @Singleton
    fun provideSmartLedApi(): SmartLedApi {
        return SmartLedApiImpl()
    }

    @Provides
    @Singleton
    fun provideSmartLedRepository(api: SmartLedApi): SmartLedRepository {
        return SmartLedRepositoryImpl(api)
    }
}