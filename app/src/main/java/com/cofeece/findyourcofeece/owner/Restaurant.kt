package com.cofeece.findyourcofeece.owner

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.cofeece.findyourcofeece.user.User
import com.cofeece.findyourcofeece.user.UserAddress
import com.google.android.gms.common.util.MapUtils
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import java.io.IOException

/** Enums: */
enum class RestaurantDetails {
    RESTAURANT, ADDRESS
}

@Parcelize
class Restaurant(private var name: String, private var address: UserAddress) : Parcelable {

//    private var latLng: LatLng? = null

    /** Constructors: */
    constructor() : this("", UserAddress())

    /** Gets: */
    fun getName(): String = name
//    fun getLatLng(): LatLng? = latLng
    fun getAddress(): UserAddress = address
//    fun getStands(): String =  stands

    /** Sets: */
    fun setName(name: String) {
        this.name = name
    }

//    fun setLatLng(latLng: LatLng?) {
//        this.latLng = latLng
//    }

    fun setAddress(address: UserAddress) {
        this.address = address
    }

    override fun equals(restaurant: Any?): Boolean {
        val restaurantToCompare = restaurant as Restaurant
        return this.name == restaurantToCompare.name &&
               this.address == restaurantToCompare.address
    }
}