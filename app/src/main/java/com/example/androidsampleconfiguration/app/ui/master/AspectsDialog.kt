package com.example.androidsampleconfiguration.app.ui.master

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidsampleconfiguration.databinding.DialogAspectsBinding
import dagger.android.support.DaggerDialogFragment

class AspectsDialog : DaggerDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        DialogAspectsBinding.inflate(inflater, container, false).apply {
            xd()
        }.root

    private fun DialogAspectsBinding.xd() {}
}
