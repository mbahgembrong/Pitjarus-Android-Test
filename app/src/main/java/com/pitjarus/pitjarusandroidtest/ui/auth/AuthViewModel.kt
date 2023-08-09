package com.pitjarus.pitjarusandroidtest.ui.auth


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pitjarus.pitjarusandroidtest.data.model.StoreResponse
import com.pitjarus.pitjarusandroidtest.repository.AuthRepository
import com.pitjarus.pitjarusandroidtest.utils.Resource
import com.pitjarus.pitjarusandroidtest.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
):ViewModel() {
    private val _responseLogin = MutableLiveData<Resource<StoreResponse>>()

    val loginResponse: LiveData<Resource<StoreResponse>>
    get() = _responseLogin

    fun login(username: String, password: String) {
        _responseLogin.postValue(Resource.loading())
        authRepository.login(username, password).observeForever {
            if (it.status == Status.SUCCESS) {
                _responseLogin.postValue(Resource.success(it.data!!))
            } else if (it.status == Status.ERROR) {
                _responseLogin.postValue(Resource.error(it.message!!))
            }
        }
    }
}

