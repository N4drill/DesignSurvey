package com.example.androidsampleconfiguration.app.ui.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.androidsampleconfiguration.app.ui.tutorial.PageInstructionFragment.PageListener

class TutorialPagerAdapter(fragmentManager: FragmentManager, private val steps: List<Step>, private val pageListener: PageListener) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = PageInstructionFragment(listener = pageListener).apply {
        arguments = Bundle().apply {
            putInt(IMAGE_BUNDLE_KEY, steps[position].drawableRes)
            putString(MESSAGE_BUNDLE_KEY, steps[position].message)
            if (position == steps.size - 1) {
                putBoolean(FINISH_BUNDLE_KEY, true)
            }
        }
    }

    override fun getCount(): Int = steps.size

    override fun getPageTitle(position: Int): CharSequence? {
        return "Krok ${position + 1}"
    }

    companion object {
        const val IMAGE_BUNDLE_KEY = "image"
        const val MESSAGE_BUNDLE_KEY = "message"
        const val FINISH_BUNDLE_KEY = "finish"
    }
}

