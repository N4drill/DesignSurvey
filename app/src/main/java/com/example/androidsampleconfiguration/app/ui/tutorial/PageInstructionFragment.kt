package com.example.androidsampleconfiguration.app.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidsampleconfiguration.databinding.PageInstructionBinding

class PageInstructionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PageInstructionBinding.inflate(inflater, container, false).root
    }
}
