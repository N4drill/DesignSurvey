package com.example.androidsampleconfiguration.app.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidsampleconfiguration.databinding.FragmentInstructionBinding
import dagger.android.support.DaggerFragment

class TutorialFragment : DaggerFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentInstructionBinding.inflate(inflater, container, false).root
}
