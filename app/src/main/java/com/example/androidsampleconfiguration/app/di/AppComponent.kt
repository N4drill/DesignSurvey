package com.example.androidsampleconfiguration.app.di

import com.example.androidsampleconfiguration.app.App
import com.example.androidsampleconfiguration.app.di.builders.ActivityBuilder
import com.example.androidsampleconfiguration.app.di.modules.AppModule
import com.example.androidsampleconfiguration.app.di.modules.AspectModule
import com.example.androidsampleconfiguration.app.di.modules.FirebaseModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        FirebaseModule::class,
        ActivityBuilder::class,
        AspectModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<App>
}


