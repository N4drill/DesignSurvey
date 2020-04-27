package com.example.androidsampleconfiguration.app.di.builders

import com.example.androidsampleconfiguration.app.di.ActivityScope
import com.example.androidsampleconfiguration.app.ui.MainActivity
import com.example.androidsampleconfiguration.app.ui.splashscreen.SplashActivity
import com.example.androidsampleconfiguration.app.ui.userform.FormActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentBuilder::class])
    abstract fun bindMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    abstract fun bindSplashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [FormActivityModule::class])
    abstract fun bindFormActivity(): FormActivity
}

@Module
class MainActivityModule

@Module
class SplashActivityModule

@Module
class FormActivityModule
