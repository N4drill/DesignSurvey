package com.example.androidsampleconfiguration.app.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.androidsampleconfiguration.R
import com.example.androidsampleconfiguration.commons.CloseAppManager
import com.example.androidsampleconfiguration.databinding.MainActivityBinding
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    private val closeAppManager = CloseAppManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)
    }

    override fun onBackPressed() {
        closeAppManager.closeAppAfterSecondTap()
    }
}
