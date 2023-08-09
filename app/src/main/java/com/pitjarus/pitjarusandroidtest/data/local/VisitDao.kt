package com.pitjarus.pitjarusandroidtest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pitjarus.pitjarusandroidtest.data.model.Visit
import java.util.Date

@Dao
interface VisitDao {
    @Query("SELECT * FROM visit")
    fun getVisits() : List<Visit>

    @Query("SELECT * FROM visit WHERE storeId = :storeId AND date = :date")
    fun getVisit(storeId: Int, date:String?): List<Visit>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVisit(visit: Visit)
}