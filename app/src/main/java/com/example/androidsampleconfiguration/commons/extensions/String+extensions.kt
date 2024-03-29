package com.example.androidsampleconfiguration.commons.extensions

import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.COLOR
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.ICONS
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.INTUITIVENESS
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.PLACEMENT
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.SIZING
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.TEXT

fun List<String>.translate(toEnglish: Boolean): List<String> = mapNotNull {
    aspectMap[it.decapitalize()]?.let { aspect ->
        when (toEnglish) {
            true -> aspect.english
            false -> aspect.polish
        }
    }?.capitalize()
}

private val aspectMap = mapOf(
    COLOR.polish to COLOR,
    SIZING.polish to SIZING,
    PLACEMENT.polish to PLACEMENT,
    INTUITIVENESS.polish to INTUITIVENESS,
    TEXT.polish to TEXT,
    ICONS.polish to ICONS,
    COLOR.english to COLOR,
    SIZING.english to SIZING,
    PLACEMENT.english to PLACEMENT,
    INTUITIVENESS.english to INTUITIVENESS,
    TEXT.english to TEXT,
    ICONS.english to ICONS
)
