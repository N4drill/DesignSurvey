package com.example.androidsampleconfiguration.app.dataaccess.model

import com.example.androidsampleconfiguration.app.ui.userform.UserModel
import com.example.androidsampleconfiguration.app.ui.userform.UserModel.Gender.FEMALE
import com.example.androidsampleconfiguration.app.ui.userform.UserModel.Gender.MALE
import com.google.firebase.firestore.PropertyName

data class UserFirestore(
    @PropertyName("gender") val gender: String = "",
    @PropertyName("age") val age: Int = 20,
    @PropertyName("profession") val profession: String = "Non Programmer",
    @PropertyName("designExperience") val designExperience: Int = 0,
    @PropertyName("answeredQuestions") val answeredQuestions: ArrayList<String> = arrayListOf()
)

fun UserFirestore.toUserModel(): UserModel = UserModel(
    gender = requireNotNull(
        when (gender) {
            "male" -> MALE
            "female" -> FEMALE
            else -> null
        }
    ),
    age = age,
    profession = profession,
    designExperience = designExperience,
    answeredQuestions = answeredQuestions.toList()
)

fun UserModel.toUserFirestore(): UserFirestore = UserFirestore(
    gender = when (gender) {
        MALE -> "male"
        FEMALE -> "female"
    },
    age = age,
    profession = profession,
    designExperience = designExperience,
    answeredQuestions = ArrayList(answeredQuestions)
)
