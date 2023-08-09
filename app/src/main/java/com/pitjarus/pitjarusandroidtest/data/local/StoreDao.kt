package com.pitjarus.pitjarusandroidtest.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pitjarus.pitjarusandroidtest.data.model.Store
import com.pitjarus.pitjarusandroidtest.data.model.StoreEntity

@Dao
interface StoreDao {

    @Query("SELECT  st.*, CASE WHEN  strftime('%m',vs.lastVisit) =strftime('%m', 'now') THEN lastVisit ELSE NULL END AS visitThisMonth, vs.lastVisit AS  lastVisit FROM store st LEFT JOIN (SELECT storeId,  MAX(date)  AS lastVisit  FROM visit GROUP BY storeId) vs ON vs.storeId=st.id WHERE st.name LIKE '%' || :search || '%'")
    fun getStores(search:String): LiveData<List<Store>>

    @Query("SELECT  st.*, CASE WHEN  strftime('%m',vs.lastVisit) =strftime('%m', 'now') THEN lastVisit ELSE NULL END AS visitThisMonth, vs.lastVisit AS  lastVisit FROM store st LEFT JOIN (SELECT storeId,  MAX(date)  AS lastVisit  FROM visit GROUP BY storeId) vs ON vs.storeId=st.id WHERE st.id=:id")
    fun getStore(id: String):LiveData<Store>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(store: StoreEntity):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stores: List<StoreEntity?>):List<Long>
}