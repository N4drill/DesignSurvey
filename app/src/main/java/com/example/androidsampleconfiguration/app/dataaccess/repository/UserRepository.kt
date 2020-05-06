package com.example.androidsampleconfiguration.app.dataaccess.repository

import com.example.androidsampleconfiguration.app.dataaccess.FirebaseService
import com.example.androidsampleconfiguration.app.dataaccess.model.toUserFirestore
import com.example.androidsampleconfiguration.app.dataaccess.model.toUserModel
import com.example.androidsampleconfiguration.app.ui.userform.UserModel
import com.google.firebase.firestore.DocumentReference
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firebaseService: FirebaseService
) {

    fun insert(userModel: UserModel): Single<DocumentReference> = firebaseService.insertUser(userModel.toUserFirestore())

    fun get(userId: String): Single<UserModel> = firebaseService.getUser(userId = userId).map { it.toUserModel() }

    fun checkUserExists(userId: String): Single<Boolean> = firebaseService.checkUserExists(userId = userId)

    fun updateAnswers(userId: String, newAnswers: List<String>): Completable = firebaseService.updateAlreadyAnswer(userId, newAnswers)
}
