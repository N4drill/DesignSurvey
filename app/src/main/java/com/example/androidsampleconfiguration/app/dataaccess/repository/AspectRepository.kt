package com.example.androidsampleconfiguration.app.dataaccess.repository

import com.example.androidsampleconfiguration.app.dataaccess.FirebaseService
import com.example.androidsampleconfiguration.app.dataaccess.model.AspectFirestore
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.COLOR
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.ICONS
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.INTUITIVITY
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.PLACEMENT
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.SIZING
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.TEXT
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
        "intuitivity" -> INTUITIVITY
        "sizing" -> SIZING
        "text" -> TEXT
        "icons" -> ICONS
        else -> null
    }
}
