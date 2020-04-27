package com.example.androidsampleconfiguration.app.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.androidsampleconfiguration.R
import com.example.androidsampleconfiguration.app.domain.SharedPreferenceManager
import com.example.androidsampleconfiguration.app.ui.MainActivity
import com.example.androidsampleconfiguration.databinding.ActivitySplashBinding
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        Timber.plant(Timber.DebugTree())
        Timber.d("Application just started")

        when (isFirstEntrance()) {
            true -> {
                Timber.d("First entrance in application, intent to form")
            }
            false -> {
                Timber.d("User id found: ${sharedPreferenceManager.getUserId()}, going to masterActivity")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun isFirstEntrance(): Boolean = sharedPreferenceManager.getUserId() == null
}
