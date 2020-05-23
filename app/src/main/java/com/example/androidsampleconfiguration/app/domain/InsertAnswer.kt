package com.example.androidsampleconfiguration.app.domain

import com.example.androidsampleconfiguration.app.dataaccess.repository.AnswerRepository
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.AnswerData
import com.example.androidsampleconfiguration.app.ui.userform.UserModel
import com.google.firebase.firestore.DocumentReference
import com.yuyakaido.android.cardstackview.Direction
import io.reactivex.Single
import javax.inject.Inject

class InsertAnswer @Inject constructor(
    private val answerRepository: AnswerRepository
) {

    fun execute(answerData: AnswerData, userModel: UserModel): Single<DocumentReference> =
        answerRepository.insert(answerData.toAnswerModel(userModel))

    private fun AnswerData.toAnswerModel(userModel: UserModel): AnswerModel = AnswerModel(
        questionId = questionId,
        answerTime = answerTime,
        dragToAnswerTime = dragToAnswerTime,
        dragFails = dragFails,
        swapDirectionChangesCount = swapDirectionChangesCount,
        firstDecision = firstDecision,
        finalDecision = finalDecision,
        user = userModel,
        selectedAspects = selectedAspects,
        rating = rating
    )
}

data class AnswerModel(
    val questionId: String,
    val answerTime: Long,
    val dragToAnswerTime: Long,
    val dragFails: Int,
    val swapDirectionChangesCount: Int,
    val firstDecision: Direction,
    val finalDecision: Direction,
    val user: UserModel,
    val selectedAspects: List<String>,
    val rating: Int
)
