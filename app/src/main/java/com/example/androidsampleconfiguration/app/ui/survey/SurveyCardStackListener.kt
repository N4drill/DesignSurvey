package com.example.androidsampleconfiguration.app.ui.survey

import android.view.View
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackListener.CardListenerEvent.OnAppeared
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackListener.CardListenerEvent.OnCanceled
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackListener.CardListenerEvent.OnDisappeared
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackListener.CardListenerEvent.OnDragging
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackListener.CardListenerEvent.OnRewound
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackListener.CardListenerEvent.OnSwiped
import com.example.androidsampleconfiguration.commons.extensions.observeOnMain
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class SurveyCardStackListener : CardStackListener {

    private val listenerEmitter: PublishSubject<CardListenerEvent> = PublishSubject.create<CardListenerEvent>()
    val listenerEvents: Observable<CardListenerEvent>
        get() = listenerEmitter.observeOnMain()

    override fun onCardDisappeared(view: View, position: Int) {
        OnDisappeared(position).addEvent()
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
        OnDragging(direction, ratio).addEvent()
    }

    override fun onCardSwiped(direction: Direction) {
        OnSwiped(direction).addEvent()
    }

    override fun onCardCanceled() {
        OnCanceled.addEvent()
    }

    override fun onCardAppeared(view: View, position: Int) {
        OnAppeared(position).addEvent()
    }

    override fun onCardRewound() {
        OnRewound.addEvent()
    }

    sealed class CardListenerEvent {
        data class OnDisappeared(val position: Int) : CardListenerEvent()
        data class OnDragging(val direction: Direction, val ratio: Float) : CardListenerEvent()
        data class OnSwiped(val direction: Direction) : CardListenerEvent()
        data class OnAppeared(val position: Int) : CardListenerEvent()
        object OnCanceled : CardListenerEvent()
        object OnRewound : CardListenerEvent()
    }

    private fun CardListenerEvent.addEvent() {
        listenerEmitter.onNext(this)
        Timber.d("Listener Event : ${javaClass.simpleName} triggered !")
    }
}
