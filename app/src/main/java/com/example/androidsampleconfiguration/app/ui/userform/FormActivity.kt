package com.example.androidsampleconfiguration.app.ui.userform

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.example.androidsampleconfiguration.R
import com.example.androidsampleconfiguration.app.dataaccess.repository.UserRepository
import com.example.androidsampleconfiguration.app.domain.SharedPreferenceManager
import com.example.androidsampleconfiguration.app.ui.MainActivity
import com.example.androidsampleconfiguration.app.ui.userform.UserModel.Gender
import com.example.androidsampleconfiguration.app.ui.userform.UserModel.Gender.FEMALE
import com.example.androidsampleconfiguration.app.ui.userform.UserModel.Gender.MALE
import com.example.androidsampleconfiguration.commons.SeekBarChangeListener
import com.example.androidsampleconfiguration.databinding.ActivityFormBinding
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

class FormActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityFormBinding

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var sharedPreferenceManager: SharedPreferenceManager

    private val onDestroyDisposable = CompositeDisposable()
    private var currentUserModel: UserModel by Delegates.observable(
        UserModel(
            gender = DEFAULT_GENDER,
            age = DEFAULT_AGE,
            designExperience = DEFAULT_EXP,
            profession = DEFAULT_PROFESSION,
            answeredQuestions = DEFAULT_ANSWERED_QUESTIONS
        )
    ) { _, _, new ->
        new.printInfo("New UserModel")
        binding.changeGenderAppearance()
        binding.tvCurrentAge.text = new.age.toString()
        binding.tvCurrentExp.text = new.designExperience.formatDesignExperience()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityFormBinding>(this, R.layout.activity_form).apply {
            binding = this
            setupDefaults()
            setupGenderSwitch()
            setupProfessionSpinner()
            setupAgeSeekBar()
            setupExpSeekBar()
            setupCta()
        }

        Timber.d("Form layout started")
    }

    private fun ActivityFormBinding.setupDefaults() {
        currentUserModel = currentUserModel // TODO: Kinda tricky....
        minAge = MIN_AGE
        minExp = MIN_EXP
        changeProgressVisibility(false)
    }

    private fun checkValidation(): Boolean = true //TODO: do I need this ?

    private fun ActivityFormBinding.setupCta() {
        btnAccept.setOnClickListener {
            changeProgressVisibility(true)
            if (checkValidation()) {
                with(currentUserModel) {
                    userRepository.insert(currentUserModel)
                        .doAfterSuccess {
                            changeProgressVisibility(false)
                            val masterIntent = Intent(this@FormActivity, MainActivity::class.java)
                            startActivity(masterIntent)
                        }
                        .subscribe({
                            Timber.d("New user inserted, fetched id: ${it.id}")
                            sharedPreferenceManager.saveUserId(it.id)
                        }, { Timber.e(it, "Error while inserting user") })

                    printInfo("Saving  UserModel")
                }
            }
        }
    }

    private fun ActivityFormBinding.setupGenderSwitch() {
        sGender.setOnCheckedChangeListener { _, isChecked ->
            updateUserModel(
                gender = when (isChecked) {
                    true -> FEMALE
                    false -> MALE
                }
            )
        }

    }

    private fun updateUserModel(
        gender: Gender = currentUserModel.gender,
        age: Int = currentUserModel.age,
        designExperience: Int = currentUserModel.designExperience,
        profession: String = currentUserModel.profession
    ) {
        currentUserModel = currentUserModel.copy(
            gender = gender,
            age = age,
            designExperience = designExperience,
            profession = profession
        )
    }

    private fun ActivityFormBinding.setupAgeSeekBar() {
        with(seekAge) {
            incrementProgressBy(AGE_STEP)
            progress = DEFAULT_AGE
            max = MAX_AGE
            setOnSeekBarChangeListener(object : SeekBarChangeListener() {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    val result = when {
                        progress < MIN_AGE -> MIN_AGE
                        progress > MAX_AGE -> MAX_AGE
                        else -> progress
                    }

                    updateUserModel(age = result)
                }
            })
        }
    }

    private fun ActivityFormBinding.setupExpSeekBar() {
        with(seekExp) {
            incrementProgressBy(EXP_STEP)
            progress = DEFAULT_EXP
            max = MAX_EXP
            setOnSeekBarChangeListener(object : SeekBarChangeListener() {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    val result = when {
                        progress < MIN_EXP -> MIN_EXP
                        progress > MAX_EXP -> MAX_EXP
                        else -> progress
                    }
                    updateUserModel(designExperience = result)
                }
            })
        }
    }

    private fun ActivityFormBinding.setupProfessionSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this@FormActivity, R.array.professions, android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerProfession.adapter = adapter
        spinnerProfession.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(spinner: AdapterView<*>, view: View?, position: Int, id: Long) {
                updateUserModel(profession = spinner.getItemAtPosition(position).toString())
            }

        }
    }

    private fun ActivityFormBinding.changeGenderAppearance() {
        when (currentUserModel.gender) {
            MALE -> {
                maleLabel.setTypeface(null, Typeface.BOLD)
                femaleLabel.setTypeface(null, Typeface.NORMAL)
            }
            FEMALE -> {
                maleLabel.setTypeface(null, Typeface.NORMAL)
                femaleLabel.setTypeface(null, Typeface.BOLD)
            }
        }
    }

    private fun changeProgressVisibility(isVisible: Boolean) {
        binding.progressVisibility = isVisible
        binding.btnAccept.isClickable = !isVisible
    }

    private fun Int.formatDesignExperience(): String = getString(
        when (this) {
            1 -> R.string.less_than_year
            2 -> R.string.one_two_years
            3 -> R.string.two_tree_years
            4 -> R.string.more_than_three
            else -> R.string.no_experience
        }
    )

    override fun onDestroy() {
        super.onDestroy()
        onDestroyDisposable.clear()
    }

    companion object {
        private const val MIN_AGE = 12
        private const val MAX_AGE = 80
        private const val AGE_STEP = 1
        private const val MIN_EXP = 0
        private const val MAX_EXP = 4
        private const val EXP_STEP = 1
        //DEFAULTS
        private val DEFAULT_GENDER = MALE
        private const val DEFAULT_AGE = 20
        private const val DEFAULT_PROFESSION = "Non Programmer" // TODO: Update to get this from resources
        private const val DEFAULT_EXP = 0
        private val DEFAULT_ANSWERED_QUESTIONS = listOf<String>()

    }
}

private fun UserModel.printInfo(message: String = " UserModel update -> ") {
    Timber.d("$message -> Gender: $gender, Age: $age, Experience: $designExperience, Profession: $profession")
}

data class UserModel(
    val gender: Gender,
    val age: Int,
    val profession: String,
    val designExperience: Int,
    val answeredQuestions: List<String>
) {

    enum class Gender {
        MALE,
        FEMALE
    }
}
