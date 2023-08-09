package com.pitjarus.pitjarusandroidtest.ui.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pitjarus.pitjarusandroidtest.data.model.Store
import com.pitjarus.pitjarusandroidtest.repository.StoreRepository
import com.pitjarus.pitjarusandroidtest.utils.Resource
import com.pitjarus.pitjarusandroidtest.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
):ViewModel() {

//    mutable live data resource
    private val _stores = MutableLiveData<Resource<List<Store>>>()
    val stores: LiveData<Resource<List<Store>>>
        get() = _stores

    private val _store = MutableLiveData<Resource<Store>>()
    val store: LiveData<Resource<Store>>
        get() = _store

    fun search(search:String){
            _stores.postValue(Resource.loading())
        storeRepository.getStores(search).observeForever {
            if(it.status == Status.SUCCESS){
                _stores.postValue(Resource.success(it.data!!))
            }else if(it.status == Status.ERROR){
                _stores.postValue(Resource.error(it.message!!))
            }
        }
    }

    fun getStore(id:String){
        storeRepository.getStore(id).observeForever {
            _store.postValue(it)
        }
    }

    fun setVisited(storeId:String){
        storeRepository.setVisited(storeId).observeForever {
            if(it.status == Status.SUCCESS){
                Timber.d("setVisited: ${it.data}")
            }else if(it.status == Status.ERROR){
                Timber.d("setVisited: ${it.message}")
            }
        }
    }

    fun setMyLocation(latitude:Double, longitude :Double){
        storeRepository.setMyLocation(latitude, longitude).observeForever {
            if(it.status == Status.SUCCESS){
                Timber.d("setMyLocation: ${it.data}")
            }else if(it.status == Status.ERROR){
                Timber.d("setMyLocation: ${it.message}")
            }
        }

    }
}