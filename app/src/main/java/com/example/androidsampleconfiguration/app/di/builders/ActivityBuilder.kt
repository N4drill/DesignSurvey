package com.example.androidsampleconfiguration.app.di.builders

import com.example.androidsampleconfiguration.app.di.ActivityScope
import com.example.androidsampleconfiguration.app.di.FragmentScope
import com.example.androidsampleconfiguration.app.ui.MainActivity
import com.example.androidsampleconfiguration.app.ui.splashscreen.SplashActivity
import com.example.androidsampleconfiguration.app.ui.tutorial.TutorialActivity
import com.example.androidsampleconfiguration.app.ui.tutorial.TutorialFragment
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

    @ActivityScope
    @ContributesAndroidInjector(modules = [TutorialActivityModule::class])
    abstract fun bindTutorialActivity(): TutorialActivity
}

@Module
class MainActivityModule

@Module
class SplashActivityModule

@Module
class FormActivityModule

@Module
abstract class TutorialActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindInstructionFragment(): TutorialFragment
}
