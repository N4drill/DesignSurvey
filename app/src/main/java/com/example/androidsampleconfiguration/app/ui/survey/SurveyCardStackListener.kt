package com.example.androidsampleconfiguration.app.ui.survey

import android.view.View
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

    override fun onCardDisappeared(view: View?, position: Int) {

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {

    }

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {

    }

    override fun onCardRewound() {

    }

    sealed class CardListenerEvent {
        object OnCardDisappeared : CardListenerEvent()
    }
}

fun String.info() = Timber.d("MYCARD : $this activated")
