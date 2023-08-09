package com.pitjarus.pitjarusandroidtest.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.room.Room

import com.pitjarus.pitjarusandroidtest.data.local.AppDatabase
import com.pitjarus.pitjarusandroidtest.data.model.Preference

import com.pitjarus.pitjarusandroidtest.data.model.Store
import com.pitjarus.pitjarusandroidtest.data.model.Visit
import com.pitjarus.pitjarusandroidtest.data.model.asDatabase
import com.pitjarus.pitjarusandroidtest.utils.Resource
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import java.text.SimpleDateFormat
import javax.inject.Inject

class StoreRepository @Inject constructor(
    private val database: AppDatabase
){

    init {
        database.preferenceDao().getPreference("longitude").observeForever {
            this.longitude = it?.value?.toDoubleOrNull()
        }
        database.preferenceDao().getPreference("latitude").observeForever {
            this.latitude = it?.value?.toDoubleOrNull()
        }
    }

    private var latitude: Double? = null
    private var longitude : Double? = null

    fun getStores(search:String)= liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val responseStatus = database.storeDao().getStores(search)
        emitSource(responseStatus.map {
            val newStore = it.map { store ->
                val distance = _distance( store.latitude, store.longitude)
                store.distance = distance
                store
            }
            Resource.success(newStore)
        })
    }
    fun setMyLocation(latitude: Double, longitude: Double) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        database.preferenceDao().delete("latitude")
       database.preferenceDao().insertPref(Preference("latitude", latitude.toString()))
        database.preferenceDao().delete("longitude")
        database.preferenceDao().insertPref(Preference("longitude", longitude.toString()))
        emit(Resource.success(true))
    }

    fun _distance(lat2: Double?, lon2: Double?): Double {
        var lat1 = latitude
        var lon1 = longitude
        Timber.d("lat1 $lat1 lon1 $lon1 lat2 $lat2 lon2 $lon2")
       if(lat1==null || lon1 == null|| lat2==null || lon2==null){
           return 0.0
         }
//      distance dalam meter
        val R = 6371e3
        val phi1 = Math.toRadians(lat1)
        val phi2 = Math.toRadians(lat2)
        val deltaPhi = Math.toRadians(lat2 - lat1)
        val deltaLambda = Math.toRadians(lon2 - lon1)
        val a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                Math.cos(phi1) * Math.cos(phi2) *
                Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val d = R * c
        return d
    }

    fun getStore(id: String)=liveData  (Dispatchers.IO){
//        Resource
        emitSource(database.storeDao().getStore(id).map {
            it.distance = _distance(it.latitude, it.longitude)
            Resource.success(it)
        })
    }

    fun update(store: Store){
        database.storeDao().insert(store.asDatabase())
    }

    fun setVisited(storeId:String)= liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val response = database.visitDao().insertVisit(Visit(storeId, SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis())))
        emit(Resource.success(true))

    }
}