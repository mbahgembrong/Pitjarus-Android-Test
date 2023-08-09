package com.pitjarus.pitjarusandroidtest.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StoreResponse(
   @SerializedName("status")  val status: String,
   @SerializedName("stores") val stores: List<Store?>? = null,
   @SerializedName("message") val message: String
)
