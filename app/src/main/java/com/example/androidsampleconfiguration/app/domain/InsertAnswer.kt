package com.example.androidsampleconfiguration.app.domain

import com.example.androidsampleconfiguration.app.dataaccess.repository.AnswerRepository
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.AnswerModel
import com.google.firebase.firestore.DocumentReference
import io.reactivex.Single
import javax.inject.Inject

class InsertAnswer @Inject constructor(
    private val answerRepository: AnswerRepository
) {

    fun execute(answerModel: AnswerModel): Single<DocumentReference> = answerRepository.insert(answerModel)
}
