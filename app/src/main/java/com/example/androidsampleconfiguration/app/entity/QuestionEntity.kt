package com.example.androidsampleconfiguration.app.entity

data class QuestionEntity(
    val id: String,
    val aspects: List<Aspect>,
    val type: Type,
    val url: String
) {

    enum class Aspect(val title: String) {
        COLOR("color"),
        PLACEMENT("placement"),
        SIZING("sizing"),
        READABILITY("readability")
    }

    enum class Type {
        LAYOUT,
        COMPONENT,
        TEXT_FIELD
    }
}
