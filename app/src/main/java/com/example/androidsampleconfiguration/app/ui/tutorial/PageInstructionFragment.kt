package com.example.androidsampleconfiguration.app.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidsampleconfiguration.R
import com.example.androidsampleconfiguration.databinding.PageInstructionBinding

class PageInstructionFragment(private val listener: PageListener) : Fragment() {

    lateinit var binding: PageInstructionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PageInstructionBinding.inflate(inflater, container, false).apply { binding = this }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val step = Step(
            drawableRes = arguments?.getInt(TutorialPagerAdapter.IMAGE_BUNDLE_KEY)
                ?: R.drawable.ic_android_black_24dp,
            message = arguments?.getString(TutorialPagerAdapter.MESSAGE_BUNDLE_KEY) ?: ""
        )
        updateLayout(step)

        val shouldShowButton = arguments?.getBoolean(TutorialPagerAdapter.FINISH_BUNDLE_KEY) ?: false
        if (shouldShowButton) {
            listener.showButton()
        }
    }

    private fun updateLayout(step: Step) {
        binding.ivStepImage.setImageResource(step.drawableRes)
        binding.tvStepMessage.text = step.message
    }

    interface PageListener {
        fun showButton()
    }
}
