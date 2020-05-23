package com.example.androidsampleconfiguration.app.domain

import com.example.androidsampleconfiguration.app.dataaccess.repository.AspectsRepository
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect
import io.reactivex.Single
import javax.inject.Inject

class GetAllAspects @Inject constructor(
    private val aspectsRepository: AspectsRepository
) {

    fun execute(): Single<List<Aspect>> {
        return aspectsRepository.getAllAvailableAspects()
    }
}
