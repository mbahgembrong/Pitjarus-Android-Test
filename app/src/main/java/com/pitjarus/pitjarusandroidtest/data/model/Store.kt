package com.pitjarus.pitjarusandroidtest.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Store(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("store_id") var id: String,
    @SerializedName("store_code") var code: String,
    @SerializedName("store_name") var name: String,
    @SerializedName("address") var address: String,
    @SerializedName("dc_id") var dcId: String,
    @SerializedName("dc_name") var dcName: String,
    @SerializedName("account_id") var accountId: String,
    @SerializedName("account_name") var accountName: String,
    @SerializedName("subchannel_id") var subChannelId: String,
    @SerializedName("subchannel_name") var subChannelName: String,
    @SerializedName("channel_id") var channelId: String,
    @SerializedName("channel_name") var channelName: String,
    @SerializedName("area_id") var areaId: String,
    @SerializedName("area_name") var areaName: String,
    @SerializedName("region_id") var regionId: String,
    @SerializedName("region_name") var regionName: String,
    @SerializedName("latitude") var latitude: Double?=null,
    @SerializedName("longitude") var longitude: Double?=null,
    var distance: Double? = null,
    var visitThisMonth: String?=null,
    val lastVisit: String?=null
):Parcelable

fun Store.asDatabase(): StoreEntity {
    return StoreEntity(
        id = id,
        code = code,
        name = name,
        address = address,
        dcId = dcId,
        dcName = dcName,
        accountId = accountId,
        accountName = accountName,
        subChannelId = subChannelId,
        subChannelName = subChannelName,
        channelId = channelId,
        channelName = channelName,
        areaId = areaId,
        areaName = areaName,
        regionId = regionId,
        regionName = regionName,
        latitude = latitude,
        longitude = longitude
    )
}
@Entity(tableName = "store")
data class StoreEntity(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("store_id") var id: String,
    @SerializedName("store_code") var code: String,
    @SerializedName("store_name") var name: String,
    @SerializedName("address") var address: String,
    @SerializedName("dc_id") var dcId: String,
    @SerializedName("dc_name") var dcName: String,
    @SerializedName("account_id") var accountId: String,
    @SerializedName("account_name") var accountName: String,
    @SerializedName("subchannel_id") var subChannelId: String,
    @SerializedName("subchannel_name") var subChannelName: String,
    @SerializedName("channel_id") var channelId: String,
    @SerializedName("channel_name") var channelName: String,
    @SerializedName("area_id") var areaId: String,
    @SerializedName("area_name") var areaName: String,
    @SerializedName("region_id") var regionId: String,
    @SerializedName("region_name") var regionName: String,
    @SerializedName("latitude") var latitude: Double?=null,
    @SerializedName("longitude") var longitude: Double?=null,
)
