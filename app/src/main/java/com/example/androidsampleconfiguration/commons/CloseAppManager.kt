package com.example.androidsampleconfiguration.commons

import android.app.Activity
import android.widget.Toast
import com.example.androidsampleconfiguration.R

class CloseAppManager(private val activity: Activity, private val timeDelay: Long = 2000L) {
    private var backTime: Long = 0L

    fun closeAppAfterSecondTap() {
        if (backTime + 2000L > System.currentTimeMillis()) {
            activity.finishAffinity()
        } else {
            Toast.makeText(activity, R.string.exit_message, Toast.LENGTH_SHORT).show()
        }
        backTime = System.currentTimeMillis()
    }
}
