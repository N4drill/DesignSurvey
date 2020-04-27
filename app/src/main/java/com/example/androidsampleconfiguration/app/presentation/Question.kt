package com.example.androidsampleconfiguration.app.presentation

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.example.androidsampleconfiguration.app.entity.QuestionEntity

data class Question(
    val id: String,
    val image: RequestBuilder<Drawable>
)

fun QuestionEntity.toQuestion(fragment: Fragment): Question {
    return Question(
        id = id,
        image = Glide.with(fragment).load(url)
    )
}

fun List<QuestionEntity>.toQuestions(fragment: Fragment): List<Question> = map { it.toQuestion(fragment) }
