package com.pitjarus.pitjarusandroidtest.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pitjarus.pitjarusandroidtest.data.model.Preference
import com.pitjarus.pitjarusandroidtest.data.model.Store
import java.util.Observable

@Dao
interface PreferenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPref(preference: Preference)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(list: List<Preference>)

    @Query("SELECT * FROM preference WHERE keyword = :keyword")
    fun getPreference(keyword: String): LiveData<Preference?>

    @Query("SELECT * FROM preference")
    fun getPreferences(): LiveData<List<Preference>>

    @Query("DELETE  FROM preference")
    fun deleteAll()



    @Query("DELETE FROM preference WHERE keyword = :keyword")
    fun delete(keyword: String)
}