package com.pitjarus.pitjarusandroidtest.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preference")
data class Preference(
    var keyword: String? = null,
    var value: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}