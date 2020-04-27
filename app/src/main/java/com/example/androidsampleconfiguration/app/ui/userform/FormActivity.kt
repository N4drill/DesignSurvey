package com.example.androidsampleconfiguration.app.ui.userform

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.androidsampleconfiguration.R
import com.example.androidsampleconfiguration.databinding.ActivityFormBinding
import dagger.android.support.DaggerAppCompatActivity

class FormActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityFormBinding>(this, R.layout.activity_form)
    }
}
