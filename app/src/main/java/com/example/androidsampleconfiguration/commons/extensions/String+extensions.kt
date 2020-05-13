package com.example.androidsampleconfiguration.commons.extensions

import com.example.androidsampleconfiguration.commons.extensions.AspectTranslation.COLOR
import com.example.androidsampleconfiguration.commons.extensions.AspectTranslation.PLACEMENT
import com.example.androidsampleconfiguration.commons.extensions.AspectTranslation.READABILITY
import com.example.androidsampleconfiguration.commons.extensions.AspectTranslation.SIZING

fun List<String>.translate(toEnglish: Boolean) = mapNotNull {
    aspectMap[it.decapitalize()]?.let { aspect ->
        when (toEnglish) {
            true -> aspect.english
            false -> aspect.polish
        }
    }?.capitalize()
}

enum class AspectTranslation(val polish: String, val english: String) {
    COLOR("kolory", "color"),
    SIZING("rozmiary", "sizing"),
    PLACEMENT("rozmieszczenie", "placement"),
    READABILITY("czytelność", "readability")
}

private val aspectMap = mapOf(
    "color" to COLOR,
    "sizing" to SIZING,
    "placement" to PLACEMENT,
    "readability" to READABILITY
)
