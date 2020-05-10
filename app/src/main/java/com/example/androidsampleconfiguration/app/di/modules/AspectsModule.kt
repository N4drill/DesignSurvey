package com.example.androidsampleconfiguration.app.di.modules

import com.example.androidsampleconfiguration.commons.extensions.observeOnMain
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Singleton

@Module
class AspectModule {

    @Provides
    @Singleton
    fun provideAspectObserver(): AspectObserver = AspectObserver()

}

class AspectObserver {
    val aspectsSubject = PublishSubject.create<List<String>>()
    val aspects: Observable<List<String>>
        get() = aspectsSubject.observeOnMain()

}
