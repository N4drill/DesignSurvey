package com.example.androidsampleconfiguration.app.dataaccess.repository

import com.example.androidsampleconfiguration.app.dataaccess.FirebaseService
import com.example.androidsampleconfiguration.app.dataaccess.model.AspectFirestore
import com.example.androidsampleconfiguration.app.entity.Question.Aspect
import com.example.androidsampleconfiguration.app.entity.Question.Aspect.COLOR
import com.example.androidsampleconfiguration.app.entity.Question.Aspect.PLACEMENT
import com.example.androidsampleconfiguration.app.entity.Question.Aspect.READABILITY
import com.example.androidsampleconfiguration.app.entity.Question.Aspect.SIZING
import com.google.firebase.firestore.DocumentReference
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class AspectsRepository @Inject constructor(
    private val firebaseService: FirebaseService
) {

    private fun DocumentReference.getAspect(): Single<AspectFirestore> =
        firebaseService.getAspect(this)

    fun getAllAspects(aspectRefs: List<DocumentReference>): Single<MutableList<Aspect?>> =
        Observable.fromIterable(aspectRefs).flatMapSingle {
            it.getAspect()
                .map { aspect -> aspect.toAspect() }
        }.toList()

    private fun AspectFirestore.toAspect(): Aspect? = when (name) {
        "color" -> COLOR
        "placement" -> PLACEMENT
        "readability" -> READABILITY
        "sizing" -> SIZING
        else -> null
    }
}
