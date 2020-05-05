package com.example.androidsampleconfiguration.app.domain

import com.example.androidsampleconfiguration.app.dataaccess.repository.UserRepository
import com.example.androidsampleconfiguration.app.ui.userform.UserModel
import io.reactivex.Single
import javax.inject.Inject

class GetCurrentUser @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferenceManager: SharedPreferenceManager
) {

    fun execute(): Single<UserModel> =
        userRepository.get(requireNotNull(sharedPreferenceManager.getUserId()))
}
