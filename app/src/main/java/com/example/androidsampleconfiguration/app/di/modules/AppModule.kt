package com.example.androidsampleconfiguration.app.di.modules

import android.content.Context
import com.example.androidsampleconfiguration.app.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(app: App): Context = app
}