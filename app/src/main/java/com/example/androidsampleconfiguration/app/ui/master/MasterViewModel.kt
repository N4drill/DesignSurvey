package com.example.androidsampleconfiguration.app.ui.master

import androidx.lifecycle.AndroidViewModel
import com.example.androidsampleconfiguration.app.App
import com.example.androidsampleconfiguration.app.domain.GetCurrentUser
import com.example.androidsampleconfiguration.app.domain.GetNotAnsweredQuestions
import com.example.androidsampleconfiguration.app.domain.InsertAnswer
import com.example.androidsampleconfiguration.app.entity.QuestionEntity
import com.example.androidsampleconfiguration.app.presentation.Question
import com.example.androidsampleconfiguration.app.presentation.toQuestions
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.Action.ItemsLoaded
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackListener.CardListenerEvent
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackListener.CardListenerEvent.OnAppeared
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackListener.CardListenerEvent.OnCanceled
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackListener.CardListenerEvent.OnDisappeared
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackListener.CardListenerEvent.OnDragging
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackListener.CardListenerEvent.OnRewound
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackListener.CardListenerEvent.OnSwiped
import com.example.androidsampleconfiguration.app.ui.userform.UserModel
import com.example.androidsampleconfiguration.commons.extensions.addTo
import com.example.androidsampleconfiguration.commons.extensions.observeOnMain
import com.example.androidsampleconfiguration.commons.extensions.subscribeOnIO
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Direction.Left
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.lang.System.currentTimeMillis
import javax.inject.Inject

class MasterViewModel @Inject constructor(
    application: App,
    getNotAnsweredQuestions: GetNotAnsweredQuestions,
    getCurrentUser: GetCurrentUser,
    private val insertAnswer: InsertAnswer,
    private val masterFragment: MasterFragment
) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private var availableQuestions: List<QuestionEntity> = emptyList()
    private lateinit var currentQuestion: QuestionEntity
    private lateinit var currentUser: UserModel

    // -- Subjects
    private val actionSubject = PublishSubject.create<Action>()
    private val commandSubject = PublishSubject.create<Command>()
    private val questionsSubject = BehaviorSubject.createDefault(emptyList<Question>())

    // -- Streams
    val actions: Observable<Action>
        get() = actionSubject.observeOnMain()
    val commands: Observable<Command>
        get() = commandSubject.observeOnMain()
    val questions: Observable<List<Question>>
        get() = questionsSubject.observeOnMain()

    private var startQuestionTime = currentTimeMillis()
    private var startDraggingTime = DEFAULT_DRAGGING_TIME
    private var swapDirectionChanged = DEFAULT_DIRECTION_CHANGE_COUNT
    private var failedToSwap = DEFAULT_FAILED_TO_SWAP
    private var firstDirection: Direction? = null
    private var currentSwapDirection: Direction? = null

    init {
        getCurrentUser.execute() // TODO: Refactor ???
            .subscribeOnIO()
            .observeOnMain()
            .subscribe({
                currentUser = it
            }, { Timber.e(it, "Failed to fetch user") })
            .addTo(compositeDisposable)

        getNotAnsweredQuestions.execute()
            .subscribeOnIO()
            .observeOnMain()
            .doAfterSuccess {
                actionSubject.onNext(ItemsLoaded)
            }
            .subscribe({
                Timber.d("Emitted new ${it.size} questions")
                questionsSubject.onNext(it.toQuestions(masterFragment))
                availableQuestions = it

                if (it.isNotEmpty()) {
                    currentQuestion = it[0]
                }

//                startNewQuestion()
            }, { Timber.e(it, "Something went wrong with QUESTIONS REPOSITORY") })
            .addTo(compositeDisposable)

    }

    private fun startNewQuestion() {
        Timber.d("SURVEY: Starting new question")
        restartTimers()
        runStartTimer()
    }

    private fun restartTimers() {
        startQuestionTime = currentTimeMillis()
        startDraggingTime = DEFAULT_DRAGGING_TIME
        swapDirectionChanged = DEFAULT_DIRECTION_CHANGE_COUNT
        failedToSwap = DEFAULT_FAILED_TO_SWAP
        firstDirection = DEFAULT_SWAP_DIRECTION
        currentSwapDirection = null
    }

    private fun runStartTimer() {
        startQuestionTime = currentTimeMillis()
    }

    private fun startDragging(direction: Direction) {
        Timber.d("SURVEY: Dragging started")
        if (startDraggingTime == DEFAULT_DRAGGING_TIME) {
            startDraggingTime = currentTimeMillis()
            Timber.d("SURVEY: Started dragging")
        }
        currentSwapDirection = when {
            currentSwapDirection == null -> direction.also {
                firstDirection = it
                swapDirectionChanged++
            }
            currentSwapDirection != direction -> direction.also {
                swapDirectionChanged++
            }
            else -> direction
        }
    }

    fun onListenerEvent(event: CardListenerEvent) = when (event) {
        is OnDisappeared -> onDisappeared(event)
        is OnDragging -> onDragging(event)
        is OnSwiped -> onSwiped(event)
        is OnAppeared -> onAppeared(event)
        OnCanceled -> onCanceled()
        OnRewound -> onRewound()
    }

    private fun onDisappeared(event: OnDisappeared) {}

    private fun onDragging(event: OnDragging) {
        startDragging(event.direction)
    }

    private fun onSwiped(event: OnSwiped) {
        Timber.d("SURVEY: Question swiped: ${event.direction}")
        sendAnswer()
    }

    private fun onAppeared(event: OnAppeared) {
        Timber.d("SURVEY: New question appeared")
        currentQuestion = availableQuestions[event.position]

        startNewQuestion()
    }

    private fun onCanceled() {
        startDraggingTime = DEFAULT_DRAGGING_TIME
        firstDirection = null
        currentSwapDirection = null
        swapDirectionChanged = DEFAULT_FAILED_TO_SWAP
        failedToSwap++
    }

    private fun onRewound() {}

    private fun sendAnswer() {
        val currentTime = currentTimeMillis()
        val answerTime = currentTime - startQuestionTime
        val lastDraggingTime = currentTime - startDraggingTime

        val model = AnswerModel(
            questionId = currentQuestion.id,
            user = currentUser,
            answerTime = answerTime,
            dragToAnswerTime = lastDraggingTime,
            dragFails = failedToSwap,
            firstDecision = requireNotNull(firstDirection),
            finalDecision = requireNotNull(currentSwapDirection),
            swapDirectionChangesCount = swapDirectionChanged
        )

        insertAnswer.execute(model)
            .subscribeOnIO()
            .observeOnMain()
            .subscribe({
                Timber.d("New Answer, inserted answer and get new id: ${it.id}")
            }, { Timber.e(it, "Error while inserting new answer") })
            .addTo(compositeDisposable)

    }

    data class AnswerModel(
        val questionId: String,
        val answerTime: Long,
        val dragToAnswerTime: Long,
        val dragFails: Int,
        val swapDirectionChangesCount: Int,
        val firstDecision: Direction,
        val finalDecision: Direction,
        val user: UserModel
    )

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

    companion object {
        private const val DEFAULT_DRAGGING_TIME = 0L
        private const val DEFAULT_DIRECTION_CHANGE_COUNT = 0
        private const val DEFAULT_FAILED_TO_SWAP = 0
        private val DEFAULT_SWAP_DIRECTION = Left
    }
}
