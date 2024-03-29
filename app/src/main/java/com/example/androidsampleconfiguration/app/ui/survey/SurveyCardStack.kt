package com.example.androidsampleconfiguration.app.ui.survey

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.example.androidsampleconfiguration.app.presentation.Question
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackAdapter.QuestionViewHolder
import com.example.androidsampleconfiguration.commons.extensions.autoNotify
import com.example.androidsampleconfiguration.databinding.ItemQuestionBinding
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeableMethod
import timber.log.Timber
import kotlin.properties.Delegates

class SurveyCardStackLayoutManager(context: Context, cardStackListener: CardStackListener) :
    CardStackLayoutManager(context, cardStackListener) {

    init {
        setStackFrom(StackFrom.Left)
        setVisibleCount(3)
        setTranslationInterval(12.0f)
        setScaleInterval(0.75f)
        setMaxDegree(40.0f)
        setOverlayInterpolator(FastOutLinearInInterpolator())
        setDirections(Direction.HORIZONTAL)
        setSwipeThreshold(0.5f)
        setCanScrollHorizontal(true)
        setCanScrollVertical(true)
        setSwipeableMethod(SwipeableMethod.Manual)
        Timber.d("MY NEW CARD STACK LISTENER: $cardStackListener")
    }
}

class SurveyCardStackAdapter : RecyclerView.Adapter<QuestionViewHolder>() {
    var questions: List<Question> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { other -> id == other.id }
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
            question.image.into(binding.ivCard)
        }
    }

}
