package com.example.androidsampleconfiguration.app.ui.master

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.androidsampleconfiguration.app.ui.DaggerViewModelFactory
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.Action
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.Action.SampleAction
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.Action.SampleObjectAction
import com.example.androidsampleconfiguration.app.ui.master.MasterViewModel.Command
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackAdapter
import com.example.androidsampleconfiguration.app.ui.survey.SurveyCardStackLayoutManager
import com.example.androidsampleconfiguration.commons.extensions.addTo
import com.example.androidsampleconfiguration.commons.extensions.exhaustivePatternCheck
import com.example.androidsampleconfiguration.databinding.FragmentMasterBinding
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class MasterFragment : DaggerFragment() {

    private val compositeDisposable = CompositeDisposable()

    private val surveyAdapter: SurveyCardStackAdapter by lazy { SurveyCardStackAdapter() }

    @Inject
    lateinit var factory: DaggerViewModelFactory<MasterViewModel>

    private val viewModel: MasterViewModel by lazy {
        ViewModelProviders.of(this@MasterFragment, factory).get(MasterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMasterBinding.inflate(inflater, container, false).apply {
        viewModel.observeViewModel()
        setupCardStack()
        Timber.d("Master Fragment created")
    }.root

    private fun FragmentMasterBinding.setupCardStack() {
        cardStack.apply {
            layoutManager = SurveyCardStackLayoutManager(context)
            adapter = surveyAdapter
        }
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
                    is SampleAction -> onAction(it)
                    SampleObjectAction -> onAction(it)
                }.exhaustivePatternCheck()
            }, { Timber.d(it, "Something went wrong observing ACTION") })
            .addTo(compositeDisposable)

        commands
            .subscribe({
                when (it) {
                    is Command.SampleCommand -> onCommand(it)
                    Command.SampleObjectCommand -> onCommand(it)
                }.exhaustivePatternCheck()
            }, { Timber.d(it, "Something went wrong observing COMMANDS") })
            .addTo(compositeDisposable)
    }

    private fun onAction(action: Action) {}
    private fun onCommand(command: Command) {}

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
