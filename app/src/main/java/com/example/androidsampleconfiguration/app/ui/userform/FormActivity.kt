package com.example.androidsampleconfiguration.app.ui.userform

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.androidsampleconfiguration.R
import com.example.androidsampleconfiguration.databinding.ActivityFormBinding
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber

class FormActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityFormBinding>(this, R.layout.activity_form).apply {
            setupLayout()
        }

        Timber.d("Form layout started")
    }

    private fun ActivityFormBinding.setupLayout() {
        setupProfessionSpinner()
    }

    private fun ActivityFormBinding.setupProfessionSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this@FormActivity, R.array.professions, android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerProfession.adapter = adapter
    }

}
