package com.example.androidsampleconfiguration.app.ui.master

import androidx.lifecycle.AndroidViewModel
import com.example.androidsampleconfiguration.app.App
import com.example.androidsampleconfiguration.app.domain.GetNotAnsweredQuestions
import com.example.androidsampleconfiguration.app.presentation.Question
import com.example.androidsampleconfiguration.app.presentation.toQuestions
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.Action.ItemsLoaded
import com.example.androidsampleconfiguration.commons.extensions.addTo
import com.example.androidsampleconfiguration.commons.extensions.observeOnMain
import com.example.androidsampleconfiguration.commons.extensions.observerOnMain
import com.example.androidsampleconfiguration.commons.extensions.subscribeOnIO
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

class MasterViewModel @Inject constructor(
    application: App,
    private val getNotAnsweredQuestions: GetNotAnsweredQuestions,
    private val masterFragment: MasterFragment
) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()

    // -- Subjects
    private val actionSubject = PublishSubject.create<Action>()
    private val commandSubject = PublishSubject.create<Command>()
    private val questionsSubject = BehaviorSubject.createDefault(emptyList<Question>())

    // -- Streams
    val actions: Observable<Action>
        get() = actionSubject.observerOnMain()
    val commands: Observable<Command>
        get() = commandSubject.observerOnMain()
    val questions: Observable<List<Question>>
        get() = questionsSubject.observerOnMain()

    init {
        getNotAnsweredQuestions.execute()
            .subscribeOnIO()
            .observeOnMain()
            .doAfterSuccess {
                actionSubject.onNext(ItemsLoaded)
            }
            .subscribe({
                Timber.d("Emitted new ${it.size} questions")
                questionsSubject.onNext(it.toQuestions(masterFragment))
            }, { Timber.e(it, "Something went wrong with QUESTIONS REPOSITORY") })
            .addTo(compositeDisposable)

    }

    //region Actions and Commands
    sealed class Action {

        object ItemsLoaded : Action()
        data class SampleAction(val data: String) : Action()
        object SampleObjectAction : Action()
    }

    sealed class Command {
        data class SampleCommand(val data: String) : Command()
        object SampleObjectCommand : Command()
    }
    //endregion

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}
