package com.example.androidsampleconfiguration.app.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.androidsampleconfiguration.R
import com.example.androidsampleconfiguration.databinding.FragmentInstructionBinding
import dagger.android.support.DaggerFragment

class TutorialFragment : DaggerFragment() {

    lateinit var binding: FragmentInstructionBinding

    private val adapter: TutorialPagerAdapter by lazy {
        TutorialPagerAdapter(fragmentManager = childFragmentManager, steps = steps)
    }

    private val viewPager: ViewPager by lazy {
        binding.vpInstructions
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentInstructionBinding.inflate(inflater, container, false).apply {
            setup()
        }.root

    private fun FragmentInstructionBinding.setup() {
        binding = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager.adapter = adapter
        binding.tlInstructions.setupWithViewPager(viewPager)
    }
}

val steps: List<Step> = listOf(
    Step(
        drawableRes = R.drawable.ic_android_black_24dp,
        message = "Message1"
    ),
    Step(
        drawableRes = R.drawable.ic_android_black_24dp,
        message = "Message2"
    ),
    Step(
        drawableRes = R.drawable.ic_android_black_24dp,
        message = "Message3"
    ),
    Step(
        drawableRes = R.drawable.ic_android_black_24dp,
        message = "Message4"
    )
)
