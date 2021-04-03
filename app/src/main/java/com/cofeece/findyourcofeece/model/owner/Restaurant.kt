package com.cofeece.findyourcofeece.model.owner

import android.os.Parcelable
import com.cofeece.findyourcofeece.model.user.UserAddress
import kotlinx.android.parcel.Parcelize

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