package com.codewithmohsen.lastnews.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesScopesModule {

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class ApplicationScope

    /**
     * get scope from application to do something independently from our coroutine scopes.
     */
    @Singleton
    @ApplicationScope
    @Provides
    fun providesCoroutineScopeForApplication(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher
            + CoroutineName("ExternalCoroutineScope") +
            CoroutineExceptionHandler { _, throwable ->
            Timber.d(throwable)
    })

}