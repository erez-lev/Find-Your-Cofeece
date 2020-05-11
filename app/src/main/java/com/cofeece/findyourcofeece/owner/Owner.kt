package com.cofeece.findyourcofeece.owner

import android.os.Parcel
import android.os.Parcelable
import com.cofeece.findyourcofeece.user.User
import kotlinx.android.parcel.Parcelize


enum class OwnerChildren {
    ACCOUNT, RESTAURANT, BANK, STANDS, RATES
}

enum class AccountDetails {
    NAME, USERNAME, EMAIL, PASSWORD
}

private const val TAG = "Owner"

class Owner(name: String,
            username: String,
            email: String,
            password: String) :
    User(name, username, email, password) {

    /** Properties: */
    private lateinit var restaurant: Restaurant
    private lateinit var bank: Bank


    companion object {
        var creationSucceed = false
    }

    /** Methods: */
    constructor() : this("", "", "", "")

    /** Gets: */
    fun getOwnerId(): String = id

    fun getRestaurant(): Restaurant = restaurant

    fun getBank(): Bank = bank


    /** Sets: */
    fun setOwnerId(id: String) {
        this.id = id
    }

    fun setRestaurant(restaurant: Restaurant) {
        this.restaurant = restaurant
    }

    fun setBank(bank: Bank) {
        this.bank = bank
    }
}
