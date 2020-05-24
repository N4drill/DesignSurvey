package com.example.androidsampleconfiguration.app.dataaccess.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class QuestionFirestore(
    val id: String = "",
    val type: DocumentReference? = null,
    val url: String = ""
)

data class AspectFirestore(
    @PropertyName("name")
    val name: String = ""
)

data class TypeFirestore(
    @PropertyName("name")
    val name: String = ""
)
