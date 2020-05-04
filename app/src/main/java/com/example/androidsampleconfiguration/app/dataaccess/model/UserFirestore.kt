package com.example.androidsampleconfiguration.app.dataaccess.model

import com.example.androidsampleconfiguration.app.ui.userform.UserModel
import com.example.androidsampleconfiguration.app.ui.userform.UserModel.Gender.FEMALE
import com.example.androidsampleconfiguration.app.ui.userform.UserModel.Gender.MALE

data class UserFirestore(
    val gender: String = "",
    val age: Int = 20,
    val profession: String = "Non Programmer",
    val designExperience: Int = 0
)

fun UserModel.toUserFirestore(): UserFirestore = UserFirestore(
    gender = when (gender) {
        MALE -> "male"
        FEMALE -> "female"
    },
    age = age,
    profession = profession,
    designExperience = designExperience
)
