package com.example.androidsampleconfiguration.app.dataaccess.repository

import com.example.androidsampleconfiguration.app.dataaccess.FirebaseService
import com.example.androidsampleconfiguration.app.dataaccess.model.QuestionFirestore
import com.example.androidsampleconfiguration.app.entity.Question
import io.reactivex.Single
import io.reactivex.rxkotlin.Singles
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val firebaseService: FirebaseService,
    private val typeRepository: TypeRepository,
    private val aspectsRepository: AspectsRepository
) {

    fun getAll(): Single<List<Question>> = firebaseService.getAllQuestions().flattenAsObservable { it }.flatMapSingle { questionFirestore ->
        questionFirestore.toQuestion()
    }.toList()

    private fun QuestionFirestore.toQuestion(): Single<Question>? {
        if (type == null) return null
        return Singles.zip(
            typeRepository.get(type),
            aspectsRepository.getAllAspects(aspects)
        ).map { (type, aspects) ->
            Question(
                id = id,
                url = url,
                type = type,
                aspects = aspects.mapNotNull { it }
            )
        }
    }
}
