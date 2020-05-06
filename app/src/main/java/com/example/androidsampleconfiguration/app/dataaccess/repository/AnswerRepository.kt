package com.example.androidsampleconfiguration.app.dataaccess.repository

import com.example.androidsampleconfiguration.app.dataaccess.FirebaseService
import com.example.androidsampleconfiguration.app.dataaccess.model.toAnswerFirestore
import com.example.androidsampleconfiguration.app.domain.AnswerModel
import com.google.firebase.firestore.DocumentReference
import io.reactivex.Single
import javax.inject.Inject

class AnswerRepository @Inject constructor(
    private val firebaseService: FirebaseService
){
    fun insert(answerModel: AnswerModel) : Single<DocumentReference> = firebaseService.insertAnswer(answerModel.toAnswerFirestore())
}
