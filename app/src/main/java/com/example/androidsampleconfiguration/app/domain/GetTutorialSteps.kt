package com.example.androidsampleconfiguration.app.domain

import android.content.Context
import com.example.androidsampleconfiguration.R
import com.example.androidsampleconfiguration.app.di.AppContext
import com.example.androidsampleconfiguration.app.ui.tutorial.Step
import javax.inject.Inject

class GetTutorialSteps @Inject constructor(
    @AppContext private val context: Context
){
    fun execute(): List<Step> = listOf(
        Step(
            drawableRes = R.drawable.ins_hello,
            message = context.getString(R.string.step1)
        ),
        Step(
            drawableRes = R.drawable.ins_wifi, // https://www.flaticon.com/free-icon/wi-fi_1848
            message = context.getString(R.string.step2)
        ),
        Step(
            drawableRes = R.drawable.ins_main,
            message = context.getString(R.string.step3)
        ),
        Step(
            drawableRes = R.drawable.ins_choose,
            message = context.getString(R.string.step4)
        ),
        Step(
            drawableRes = R.drawable.ins_pause,
            message = context.getString(R.string.step5)
        ),
        Step(
            drawableRes = R.drawable.ins_instructions,
            message = context.getString(R.string.step6)
        ),
        Step(
            drawableRes = R.drawable.ints_aspects_whole,
            message = context.getString(R.string.step7)
        ),
        Step(
            drawableRes = R.drawable.ins_aspects_list,
            message = context.getString(R.string.step8)
        ),
        Step(
            drawableRes = R.drawable.ins_rating,
            message = context.getString(R.string.step9)
        ),
        Step(
            drawableRes = R.drawable.ins_aspects_accept,
            message = context.getString(R.string.step10)
        ),
        Step(
            drawableRes = R.drawable.ins_thanks,
            message = context.getString(R.string.step11)
        )
    )
}
