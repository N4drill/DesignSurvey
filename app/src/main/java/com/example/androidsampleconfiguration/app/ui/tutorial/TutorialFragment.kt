package com.example.androidsampleconfiguration.app.ui.tutorial

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.androidsampleconfiguration.app.domain.GetTutorialSteps
import com.example.androidsampleconfiguration.app.ui.MainActivity
import com.example.androidsampleconfiguration.commons.OnTabSelectListener
import com.example.androidsampleconfiguration.databinding.FragmentInstructionBinding
import com.google.android.material.tabs.TabLayout.Tab
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TutorialFragment : DaggerFragment() {

    @Inject
    lateinit var tutorialActivity: TutorialActivity

    @Inject
    lateinit var getTutorialSteps: GetTutorialSteps

    private val steps: List<Step> by lazy { getTutorialSteps.execute() }

    lateinit var binding: FragmentInstructionBinding

    private val adapter: TutorialPagerAdapter by lazy {
        TutorialPagerAdapter(
            fragmentManager = childFragmentManager,
            steps = steps
        )
    }

    private val viewPager: ViewPager by lazy {
        binding.vpInstructions
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentInstructionBinding.inflate(inflater, container, false).apply {
            setup()
        }.root

    private fun FragmentInstructionBinding.setup() {
        buttonVisible = false
        binding = this

        btnStart.setOnClickListener {
            runApplication()
        }
    }

    private fun runApplication() {
        val masterIntent = Intent(tutorialActivity, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(masterIntent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager.adapter = adapter
        binding.tlInstructions.setupWithViewPager(viewPager)
        binding.tlInstructions.addOnTabSelectedListener(object : OnTabSelectListener() {
            override fun onTabSelected(tab: Tab) {
                if (tab.position == steps.size - 1) {
                    binding.buttonVisible = true
                }
            }
        })
    }
}

