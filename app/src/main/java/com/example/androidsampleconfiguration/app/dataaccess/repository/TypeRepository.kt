package com.example.androidsampleconfiguration.app.dataaccess.repository

import com.example.androidsampleconfiguration.app.dataaccess.FirebaseService
import com.example.androidsampleconfiguration.app.dataaccess.model.TypeFirestore
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Type
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Type.COMPONENT
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Type.LAYOUT
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Type.NAVIGATION
import com.google.firebase.firestore.DocumentReference
import io.reactivex.Single
import javax.inject.Inject

class TypeRepository @Inject constructor(
    private val firebaseService: FirebaseService
) {

    fun get(typeReference: DocumentReference): Single<Type> {
        return firebaseService.getType(typeReference = typeReference).map { it.toType() }
    }

    private fun TypeFirestore.toType(): Type? = when (name) {
        "component" -> COMPONENT
        "layout" -> LAYOUT
        "navigation" -> NAVIGATION
        else -> null
    }
}
