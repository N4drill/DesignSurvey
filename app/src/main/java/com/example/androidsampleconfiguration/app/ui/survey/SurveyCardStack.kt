package com.example.androidsampleconfiguration.app.ui.survey

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidsampleconfiguration.app.presentation.Question
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackAdapter.QuestionViewHolder
import com.example.androidsampleconfiguration.commons.extensions.autoNotify
import com.example.androidsampleconfiguration.databinding.ItemQuestionBinding
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import kotlin.properties.Delegates

class SurveyCardStackLayoutManager(
    private val context: Context
) : CardStackLayoutManager(context) {
    init {

    }
}

class SurveyCardStackAdapter : RecyclerView.Adapter<QuestionViewHolder>() {
    var questions: List<Question> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { other -> name == other.name }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder =
        QuestionViewHolder(
            ItemQuestionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun getItemCount(): Int = questions.size

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(question = questions[position])
    }

    class QuestionViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Question) {

        }
    }

}