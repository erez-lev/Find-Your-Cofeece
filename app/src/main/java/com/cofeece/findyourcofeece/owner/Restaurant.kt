package com.cofeece.findyourcofeece.owner

import android.location.Address
import android.os.Parcelable
import com.cofeece.findyourcofeece.user.UserAddress
import kotlinx.android.parcel.Parcelize

enum class RestaurantDetails {
    RESTAURANT, ADDRESS
}

@Parcelize
class Restaurant(private var name: String, private var address: UserAddress) : Parcelable {

    constructor(): this("",UserAddress())

    /** Gets: */
    fun getName(): String = name
    fun getAddress(): UserAddress = address
//    fun getStands(): String =  stands

    /** Sets: */
    fun setAddress(address: UserAddress) {
        this.address = address
    }
}