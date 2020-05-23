package com.example.androidsampleconfiguration.app.di.modules

import com.example.androidsampleconfiguration.app.entity.DialogData
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
    val aspectsSubject = PublishSubject.create<DialogData>()
    val aspects: Observable<DialogData>
        get() = aspectsSubject.observeOnMain()
}
