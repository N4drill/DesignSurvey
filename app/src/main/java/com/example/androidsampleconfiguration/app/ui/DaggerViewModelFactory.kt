package com.example.androidsampleconfiguration.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
open class DaggerViewModelFactory<T> @Inject constructor(private val viewModelProvider: Provider<T>) : Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModelProvider.get() as T
}
