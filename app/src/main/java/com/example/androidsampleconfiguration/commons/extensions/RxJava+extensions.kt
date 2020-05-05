package com.example.androidsampleconfiguration.commons.extensions

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <T> PublishSubject<T>.observeOnMain(): Observable<T> =
    observeOn(AndroidSchedulers.mainThread())

fun <T> BehaviorSubject<T>.observeOnMain(): Observable<T> =
    observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.subscribeOnComputation(): Observable<T> =
    subscribeOn(Schedulers.computation())

fun <T> Observable<T>.subscribeOnIO(): Observable<T> =
    subscribeOn(Schedulers.io())

fun <T> Single<T>.subscribeOnComputation(): Single<T> =
    subscribeOn(Schedulers.computation())

fun <T> Single<T>.subscribeOnIO(): Single<T> =
    subscribeOn(Schedulers.io())

fun <T> Single<T>.observeOnMain(): Single<T> =
    observeOn(AndroidSchedulers.mainThread())
