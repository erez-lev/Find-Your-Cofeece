package com.cofeece.findyourcofeece.owner

import android.location.Address
import com.cofeece.findyourcofeece.user.UserAddress

enum class RestaurantDetails {
    RESTAURANT, ADDRESS
}

class Restaurant(private var name: String, private var address: UserAddress) {

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