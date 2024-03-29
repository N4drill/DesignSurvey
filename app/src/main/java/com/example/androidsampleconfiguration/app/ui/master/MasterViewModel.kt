package com.example.androidsampleconfiguration.app.ui.master

import androidx.lifecycle.AndroidViewModel
import com.example.androidsampleconfiguration.app.App
import com.example.androidsampleconfiguration.app.dataaccess.repository.UserRepository
import com.example.androidsampleconfiguration.app.domain.GetAllAspects
import com.example.androidsampleconfiguration.app.domain.GetCurrentUser
import com.example.androidsampleconfiguration.app.domain.GetNotAnsweredQuestions
import com.example.androidsampleconfiguration.app.domain.InsertAnswer
import com.example.androidsampleconfiguration.app.domain.SharedPreferenceManager
import com.example.androidsampleconfiguration.app.entity.QuestionEntity
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect
import com.example.androidsampleconfiguration.app.presentation.Question
import com.example.androidsampleconfiguration.app.presentation.toQuestions
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.Action.AllQuestionSolved
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.Action.ItemsLoaded
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.Action.QuestionSwiped
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.Action.SurveyReady
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
import com.yuyakaido.android.cardstackview.Direction.Right
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.lang.System.currentTimeMillis
import javax.inject.Inject

class MasterViewModel @Inject constructor(
    application: App,
    getAllAspects: GetAllAspects,
    getNotAnsweredQuestions: GetNotAnsweredQuestions,
    private val getCurrentUser: GetCurrentUser,
    private val sharedPreferenceManager: SharedPreferenceManager,
    private val userRepository: UserRepository,
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

    lateinit var availableAspects: List<Aspect>

    private var startQuestionTime = currentTimeMillis()
    private var startDraggingTime = DEFAULT_DRAGGING_TIME
    private var startAfterCancelTime = currentTimeMillis()
    private var swapDirectionChanged = DEFAULT_DIRECTION_CHANGE_COUNT
    private var failedToSwap = DEFAULT_FAILED_TO_SWAP
    private var firstDirection: Direction? = null
    private var currentSwapDirection: Direction? = null
    private var endQuestionTime = currentTimeMillis()

    private var nextReserved: Int = 0

    init {
        getAllAspects.execute()
            .subscribeOnIO()
            .observeOnMain()
            .subscribe({
                availableAspects = it
            }, { Timber.d("Error while getting all available Aspects") })
            .addTo(compositeDisposable)

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
                    actionSubject.onNext(SurveyReady)
                } else {
                    actionSubject.onNext(AllQuestionSolved)
                }

            }, { Timber.e(it, "Something went wrong with QUESTIONS REPOSITORY") })
            .addTo(compositeDisposable)

    }

    fun startNewQuestion() {
        Timber.d("SURVEY: Starting new question")
        restartMeasurements()
        runStartTimer()
    }

    private fun restartMeasurements() {
        startQuestionTime = currentTimeMillis()
        endQuestionTime = currentTimeMillis()
        startDraggingTime = DEFAULT_DRAGGING_TIME
        startAfterCancelTime = currentTimeMillis()
        swapDirectionChanged = DEFAULT_DIRECTION_CHANGE_COUNT
        failedToSwap = DEFAULT_FAILED_TO_SWAP
        firstDirection = DEFAULT_SWAP_DIRECTION
        currentSwapDirection = null
    }

    private fun runStartTimer() {
        startQuestionTime = currentTimeMillis()
    }

    private fun startDragging(direction: Direction) {
        startAfterCancelTime = currentTimeMillis()
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
        if (event.direction == Left || event.direction == Right) {
            startDragging(event.direction)
        }
    }

    private fun onSwiped(event: OnSwiped) {
        Timber.d("SURVEY: Question swiped: ${event.direction}")
        endQuestionTime = currentTimeMillis()
        actionSubject.onNext(QuestionSwiped(currentQuestion, availableAspects, event.direction))
    }

    private fun onAppeared(event: OnAppeared) {
        Timber.d("SURVEY: New question appeared")
        nextReserved = event.position
    }

    private fun onCanceled() {
        startAfterCancelTime = currentTimeMillis()
        failedToSwap++
    }

    private fun onRewound() {}

    fun sendAnswer(aspectsSelected: List<String>, rating: Int) {
        Timber.d("Send answer get: $aspectsSelected")
        val answerTime = endQuestionTime - startQuestionTime
        val lastDraggingTime = endQuestionTime - startDraggingTime
        val cancelToAnswerTime = endQuestionTime - startAfterCancelTime

        val model = AnswerData(
            questionId = currentQuestion.id,
            answerTime = answerTime,
            dragToAnswerTime = lastDraggingTime,
            dragFails = failedToSwap,
            firstDecision = requireNotNull(firstDirection),
            finalDecision = requireNotNull(currentSwapDirection),
            swapDirectionChangesCount = swapDirectionChanged,
            selectedAspects = aspectsSelected,
            rating = rating,
            cancelToAnswerTime = cancelToAnswerTime
        )

        currentQuestion = availableQuestions[nextReserved]
        startNewQuestion()

        getCurrentUser.execute()
            .retry(5)
            .flatMap { user ->
                insertAnswer.execute(model, user).map { user }
            }.flatMapCompletable { user ->
                userRepository.updateAnswers(
                    sharedPreferenceManager.getUserId()!!,
                    user.answeredQuestions + model.questionId
                )
            }.subscribe({
            }, { Timber.e(it, "Something went wrong while inserting new answer") })
            .addTo(compositeDisposable)
    }

    data class AnswerData(
        val questionId: String,
        val answerTime: Long,
        val dragToAnswerTime: Long,
        val dragFails: Int,
        val swapDirectionChangesCount: Int,
        val firstDecision: Direction,
        val finalDecision: Direction,
        val selectedAspects: List<String>,
        val rating: Int,
        val cancelToAnswerTime: Long
    )

    //region Actions and Commands
    sealed class Action {

        object ItemsLoaded : Action()
        data class QuestionSwiped(
            val question: QuestionEntity,
            val availableAspects: List<Aspect>,
            val direction: Direction
        ) : Action()

        object SurveyReady : Action()
        object AllQuestionSolved : Action()
    }

    sealed class Command
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
