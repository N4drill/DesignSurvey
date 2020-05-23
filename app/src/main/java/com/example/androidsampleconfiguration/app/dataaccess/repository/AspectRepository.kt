package com.example.androidsampleconfiguration.app.dataaccess.repository

import com.example.androidsampleconfiguration.app.dataaccess.FirebaseService
import com.example.androidsampleconfiguration.app.dataaccess.model.AspectFirestore
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.COLOR
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.ICONS
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.INTUITIVENESS
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

    fun getAllAvailableAspects(): Single<List<Aspect>> =
        firebaseService.getAllAspects().map {
            it.map { aspectFirestore -> aspectFirestore.toAspect() }
        }

    private fun AspectFirestore.toAspect(): Aspect = when (name) {
        COLOR.english -> COLOR
        PLACEMENT.english -> PLACEMENT
        INTUITIVENESS.english -> INTUITIVENESS
        SIZING.english -> SIZING
        TEXT.english -> TEXT
        ICONS.english -> ICONS
        else -> throw WrongAspectException
    }
}

object WrongAspectException : Exception() {
    override val message: String?
        get() = "Zly aspect"
}
