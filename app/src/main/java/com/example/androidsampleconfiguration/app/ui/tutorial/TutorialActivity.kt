package com.example.androidsampleconfiguration.app.ui.tutorial

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.androidsampleconfiguration.R
import com.example.androidsampleconfiguration.commons.CloseAppManager
import com.example.androidsampleconfiguration.databinding.ActivityTutorialBinding
import dagger.android.support.DaggerAppCompatActivity

class TutorialActivity : DaggerAppCompatActivity() {

    private val closeAppManager = CloseAppManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityTutorialBinding>(this, R.layout.activity_tutorial)
    }

    override fun onBackPressed() {
        closeAppManager.closeAppAfterSecondTap()
    }
}
