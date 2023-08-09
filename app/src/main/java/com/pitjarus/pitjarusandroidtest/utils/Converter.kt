package com.pitjarus.pitjarusandroidtest.utils



fun distaneConverter(distance:Double):String{
    return if (distance<1000){
        "${distance.toInt()} m"
    }else{
        "${(distance/1000).toInt()} km"
    }
}