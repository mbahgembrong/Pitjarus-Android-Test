package com.pitjarus.pitjarusandroidtest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pitjarus.pitjarusandroidtest.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
   private val authRepository: AuthRepository,
):ViewModel() {
    val isLogin= authRepository.isLogin
    val logout = authRepository.logout


}