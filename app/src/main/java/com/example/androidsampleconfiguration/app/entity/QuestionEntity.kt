package com.example.androidsampleconfiguration.app.entity


data class QuestionEntity(
    val id: String,
    val aspects: List<Aspect>,
    val type: Type,
    val url: String
) {

    enum class Aspect(val polish: String, val english: String) {
        COLOR("kolory", "color"),
        SIZING("rozmiary", "sizing"),
        PLACEMENT("rozmieszczenie", "placement"),
        INTUITIVENESS("intuicyjność", "intuitiveness"),
        TEXT("teksty", "text"),
        ICONS("ikony", "icons")
    }

    enum class Type {
        LAYOUT,
        COMPONENT,
        NAVIGATION
    }
}
