package com.cofeece.findyourcofeece.owner

import android.os.Parcelable
import com.cofeece.findyourcofeece.user.UserAddress
import kotlinx.android.parcel.Parcelize

/** Enums: */
enum class RestaurantDetails {
    RESTAURANT, ADDRESS
}

@Parcelize
class Restaurant(private var name: String, private var address: UserAddress) : Parcelable {

    /** Constructors: */
    constructor() : this("", UserAddress())

    /** Gets: */
    fun getName(): String = name
    fun getAddress(): UserAddress = address
//    fun getStands(): String =  stands

    /** Sets: */
    fun setAddress(address: UserAddress) {
        this.address = address
    }

    override fun equals(restaurant: Any?): Boolean {
        val restaurantToCompare = restaurant as Restaurant
        return this.name == restaurantToCompare.name &&
               this.address == restaurantToCompare.address
    }
}