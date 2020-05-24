package com.example.androidsampleconfiguration.app.domain

import com.example.androidsampleconfiguration.app.dataaccess.repository.AspectsRepository
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.COLOR
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.ICONS
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.INTUITIVENESS
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.PLACEMENT
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.SIZING
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.TEXT
import io.reactivex.Single
import javax.inject.Inject

class GetAllAspects @Inject constructor(
    private val aspectsRepository: AspectsRepository
) {

    fun execute(): Single<List<Aspect>> {
//        return aspectsRepository.getAllAvailableAspects()
        return Single.just(
            listOf(
                COLOR,
                INTUITIVENESS,
                PLACEMENT,
                SIZING,
                TEXT,
                ICONS
            )
        )
    }
}
