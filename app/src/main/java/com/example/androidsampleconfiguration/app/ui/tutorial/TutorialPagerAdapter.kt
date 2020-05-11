package com.example.androidsampleconfiguration.app.ui.tutorial

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TutorialPagerAdapter(fragmentManager: FragmentManager, private val steps: List<Step>) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return PageInstructionFragment()
    }

    override fun getCount(): Int = steps.size

    override fun getPageTitle(position: Int): CharSequence? {
        return "Krok $position"
    }
}

