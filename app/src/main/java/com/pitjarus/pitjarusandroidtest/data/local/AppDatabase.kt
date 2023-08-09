package com.pitjarus.pitjarusandroidtest.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import com.pitjarus.pitjarusandroidtest.data.model.Preference
import com.pitjarus.pitjarusandroidtest.data.model.Store
import com.pitjarus.pitjarusandroidtest.data.model.StoreEntity
import com.pitjarus.pitjarusandroidtest.data.model.Visit
import java.time.LocalDate
import java.util.Date


@Database(entities = [StoreEntity::class, Preference::class, Visit::class], version = 7, exportSchema = true)
abstract class AppDatabase:RoomDatabase() {
    abstract fun storeDao():StoreDao
    abstract fun preferenceDao():PreferenceDao

    abstract fun visitDao():VisitDao

}