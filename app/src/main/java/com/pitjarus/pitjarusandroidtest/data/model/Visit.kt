package com.pitjarus.pitjarusandroidtest.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

@Entity(tableName = "visit")
data class Visit (
    var storeId: String,
    var date: String,
){
    @PrimaryKey(autoGenerate = true)
    var id: Int=0
}
