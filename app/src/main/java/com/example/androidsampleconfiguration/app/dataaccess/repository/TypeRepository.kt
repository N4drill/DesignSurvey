package com.example.androidsampleconfiguration.app.dataaccess.repository

import com.example.androidsampleconfiguration.app.dataaccess.FirebaseService
import com.example.androidsampleconfiguration.app.dataaccess.model.TypeFirestore
import com.example.androidsampleconfiguration.app.entity.Question.Type
import com.example.androidsampleconfiguration.app.entity.Question.Type.COMPONENT
import com.example.androidsampleconfiguration.app.entity.Question.Type.LAYOUT
import com.example.androidsampleconfiguration.app.entity.Question.Type.TEXT_FIELD
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
        "textfield" -> TEXT_FIELD
        else -> null
    }
}
