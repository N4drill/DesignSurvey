package com.example.androidsampleconfiguration.commons.extensions

import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.COLOR
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.ICONS
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.INTUITIVITY
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.PLACEMENT
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.SIZING
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect.TEXT

fun List<String>.translate(toEnglish: Boolean) = mapNotNull {
    aspectMap[it.decapitalize()]?.let { aspect ->
        when (toEnglish) {
            true -> aspect.english
            false -> aspect.polish
        }
    }?.capitalize()
}

private val aspectMap = mapOf(
    "color" to COLOR,
    "sizing" to SIZING,
    "placement" to PLACEMENT,
    "readability" to INTUITIVITY,
    "text" to TEXT,
    "icons" to ICONS
)
