package com.example.androidsampleconfiguration.app.domain

import com.example.androidsampleconfiguration.app.dataaccess.repository.QuestionRepository
import com.example.androidsampleconfiguration.app.entity.QuestionEntity
import io.reactivex.Single
import javax.inject.Inject

class GetNotAnsweredQuestions @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val getCurrentUser: GetCurrentUser
) {

    fun execute(): Single<List<QuestionEntity>> =
        getCurrentUser.execute().flatMap { currentUser ->
            questionRepository.getAll()
                .map { allQuestions ->
                    allQuestions.filter { question ->
                        !currentUser.answeredQuestions.contains(question.id)
                    }
                }
        }
}
