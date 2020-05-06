package com.example.androidsampleconfiguration.app.dataaccess.model

import com.example.androidsampleconfiguration.app.domain.AnswerModel
import com.google.firebase.firestore.PropertyName
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Direction.Left
import com.yuyakaido.android.cardstackview.Direction.Right

data class AnswerFirestore(
    @PropertyName("question") val questionId: String = "",
    @PropertyName("time") val answerTime: Long = 0L,
    @PropertyName("dragtime") val dragToAnswerTime: Long = 0L,
    @PropertyName("directionchangecount") val directionChangesCount: Int = 0,
    @PropertyName("dragfails") val dragFails: Int = 0,
    @PropertyName("firstdecision") val firstDecision: String = "",
    @PropertyName("finaldecision") val finalDecision: String = "",
    @PropertyName("user") val user: UserFirestore? = null
)

fun AnswerModel.toAnswerFirestore(): AnswerFirestore = AnswerFirestore(
    questionId = questionId,
    user = user.toUserFirestore(),
    answerTime = answerTime,
    dragToAnswerTime = dragToAnswerTime,
    directionChangesCount = swapDirectionChangesCount,
    dragFails = dragFails,
    firstDecision = firstDecision.toFirestoreName(),
    finalDecision = finalDecision.toFirestoreName()
)

fun Direction.toFirestoreName(): String = when (this) {
    Left -> "negative"
    Right -> "positive"
    else -> ""
}
