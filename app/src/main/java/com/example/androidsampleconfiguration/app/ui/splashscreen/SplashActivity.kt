package com.example.androidsampleconfiguration.app.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.androidsampleconfiguration.R
import com.example.androidsampleconfiguration.app.dataaccess.repository.UserRepository
import com.example.androidsampleconfiguration.app.domain.SharedPreferenceManager
import com.example.androidsampleconfiguration.app.ui.MainActivity
import com.example.androidsampleconfiguration.app.ui.userform.FormActivity
import com.example.androidsampleconfiguration.commons.extensions.addTo
import com.example.androidsampleconfiguration.commons.extensions.observeOnMain
import com.example.androidsampleconfiguration.databinding.ActivitySplashBinding
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var sharedPreferenceManager: SharedPreferenceManager

    @Inject
    lateinit var userRepository: UserRepository

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        Timber.plant(Timber.DebugTree())
        Timber.d("Application just started")

        isFirstEntrance()
            .observeOnMain()
            .subscribe({ isFirst ->
                when (isFirst) {
                    true -> {
                        Timber.d("First entrance in application, intent to form")
                        val formIntent = Intent(this, FormActivity::class.java)
                        startActivity(formIntent)
                    }
                    false -> {
                        Timber.d("User id found: ${sharedPreferenceManager.getUserId()}, going to masterActivity")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }, { Timber.e(it) })
            .addTo(compositeDisposable)

    }

    private fun isFirstEntrance(): Single<Boolean> = sharedPreferenceManager.getUserId()?.let { userId ->
        userRepository.checkUserExists(userId = userId).map { !it }
    } ?: Single.just(true)

}
