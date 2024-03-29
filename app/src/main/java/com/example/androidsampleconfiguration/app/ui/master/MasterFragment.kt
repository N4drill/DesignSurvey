package com.example.androidsampleconfiguration.app.ui.master

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.androidsampleconfiguration.R
import com.example.androidsampleconfiguration.app.di.modules.AspectObserver
import com.example.androidsampleconfiguration.app.entity.QuestionEntity
import com.example.androidsampleconfiguration.app.entity.QuestionEntity.Aspect
import com.example.androidsampleconfiguration.app.ui.DaggerViewModelFactory
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.Action.AllQuestionSolved
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.Action.ItemsLoaded
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.Action.QuestionSwiped
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.Action.SurveyReady
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackAdapter
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackLayoutManager
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackListener
import com.example.androidsampleconfiguration.app.ui.tutorial.TutorialActivity
import com.example.androidsampleconfiguration.commons.extensions.addTo
import com.example.androidsampleconfiguration.commons.extensions.exhaustivePatternCheck
import com.example.androidsampleconfiguration.databinding.FragmentMasterBinding
import com.yuyakaido.android.cardstackview.Direction
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class MasterFragment : DaggerFragment() {
    private val compositeDisposable = CompositeDisposable()
    private val surveyAdapter: SurveyCardStackAdapter by lazy { SurveyCardStackAdapter() }

    @Inject
    lateinit var factory: DaggerViewModelFactory<MasterViewModel>

    @Inject
    lateinit var aspectObserver: AspectObserver

    lateinit var binding: FragmentMasterBinding

    private val viewModel: MasterViewModel by lazy {
        ViewModelProviders.of(this@MasterFragment, factory).get(MasterViewModel::class.java)
    }

    private val surveyCardStackListener by lazy {
        SurveyCardStackListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMasterBinding.inflate(inflater, container, false).apply {
        binding = this
        viewModel.observeViewModel()
        initBinding()
        setupCardStack()
        setupHideLayout()
        observeListenerEvents()
        observeAspects()
        Timber.d("Master Fragment created")
    }.root

    private fun FragmentMasterBinding.initBinding() {
        progressVisible = true
        ivPause.setOnClickListener {
            changeHideVisibility(true)
        }
        ivInformation.setOnClickListener {
            val tutorialIntent = Intent(activity, TutorialActivity::class.java)
            startActivity(tutorialIntent)
        }
    }

    private fun FragmentMasterBinding.setupCardStack() {
        cardStack.apply {
            layoutManager = SurveyCardStackLayoutManager(context, surveyCardStackListener)
            adapter = surveyAdapter
        }
    }

    private fun FragmentMasterBinding.setupHideLayout() {
        buttonHideClickable = false
        contentHideVisible = true
        btnHide.setOnClickListener {
            changeHideVisibility(false)
        }
    }

    private fun changeHideVisibility(visible: Boolean) {
        binding.contentHideVisible = visible
        if (!visible) {
            viewModel.startNewQuestion()
        }
    }

    private fun observeAspects() {
        aspectObserver.aspects.subscribe({
            Timber.d("GOT NEW ASPECTS, DIALOG TRIGGERED!: $it")
            onAspectsDialogCall(selectedAspects = it.selectedAspects, rating = it.rating)
        }, { Timber.e(it, "Something went wrong watching ASPECTS") })
            .addTo(compositeDisposable)
    }

    private fun observeListenerEvents() {
        surveyCardStackListener.listenerEvents
            .subscribe({
                viewModel.onListenerEvent(it)
            }, { Timber.e(it, "Something went wrong observing LISTENER EVENTS") })
            .addTo(compositeDisposable)
    }

    private fun MasterViewModel.observeViewModel() {
        questions
            .subscribe({
                surveyAdapter.questions = it
            }, { Timber.e(it, "Something went wrong observing QUESTIONS") })
            .addTo(compositeDisposable)

        actions
            .subscribe({
                when (it) {
                    ItemsLoaded -> onItemsLoaded()
                    is QuestionSwiped -> onQuestionSwiped(
                        question = it.question,
                        availableAspects = it.availableAspects,
                        direction = it.direction
                    )
                    SurveyReady -> onSurveyReady()
                    AllQuestionSolved -> onAllQuestionSolved()
                }.exhaustivePatternCheck()
            }, { Timber.d(it, "Something went wrong observing ACTION") })
            .addTo(compositeDisposable)

        commands
            .subscribe({
            }, { Timber.d(it, "Something went wrong observing COMMANDS") })
            .addTo(compositeDisposable)
    }

    private fun onAspectsDialogCall(selectedAspects: List<String>, rating: Int) {
        viewModel.sendAnswer(selectedAspects, rating)
    }

    private fun onAllQuestionSolved() {
        changeHideVisibility(false)
        with(binding) {
            ivInformation.visibility = View.INVISIBLE
            ivPause.visibility = View.INVISIBLE
            infoImgLeft.visibility = View.INVISIBLE
            infoLeft.visibility = View.INVISIBLE
            infoRight.visibility = View.INVISIBLE
            infoImgRight.visibility = View.INVISIBLE
            tvInstructionTitle.visibility = View.INVISIBLE
            tvPauseTitle.visibility = View.INVISIBLE
        }
    }

    private fun onQuestionSwiped(question: QuestionEntity, availableAspects: List<Aspect>, direction: Direction) {
        val navController = findNavController()
        val swipedRight = direction == Direction.Right
        val action = MasterFragmentDirections.masterToDialog(
            aspects = availableAspects.map { it.english }.toTypedArray(),
            imageUrl = question.url,
            swipedRight = swipedRight
        )
        navController.navigate(action)
    }

    override fun onPause() {
        super.onPause()
        changeHideVisibility(true)
    }

    private fun onSurveyReady() {
        with(binding) {
            buttonHideClickable = true
            tvHideMessage.setText(R.string.survey_ready)
            btnHide.setText(R.string.hide_button_ready)
        }
    }

    private fun onItemsLoaded() {
        with(binding) {
            progressVisible = false
            tvMessage.text = getString(R.string.thanks_message)
            tvMessage.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
