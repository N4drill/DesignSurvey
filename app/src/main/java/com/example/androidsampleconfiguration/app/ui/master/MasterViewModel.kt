package com.example.androidsampleconfiguration.app.ui.master

import androidx.lifecycle.AndroidViewModel
import com.example.androidsampleconfiguration.app.App
import com.example.androidsampleconfiguration.app.dataaccess.repository.QuestionRepository
import com.example.androidsampleconfiguration.app.presentation.Question
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
    questionRepository: QuestionRepository
) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()

    // -- Subjects
    private val actionSubject = PublishSubject.create<Action>()
    private val commandSubject = PublishSubject.create<Command>()
    private val questionsSubject = BehaviorSubject.createDefault((1..100).map { Question("x") })

    // -- Streams
    val actions: Observable<Action>
        get() = actionSubject.observerOnMain()
    val commands: Observable<Command>
        get() = commandSubject.observerOnMain()
    val questions: Observable<List<Question>>
        get() = questionsSubject.observerOnMain()

    init {
        questionRepository.getAll()
            .subscribeOnIO()
            .observeOnMain()
            .subscribe({
                Timber.d("$it")
            }, { Timber.e(it, "Something went wrong with QUESTIONS REPOSITORY") })
            .addTo(compositeDisposable)

    }

    //region Actions and Commands
    sealed class Action {

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
