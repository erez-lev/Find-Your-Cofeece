package com.cofeece.findyourcofeece.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

enum class AddressDetails {
    STREET, CITY, COUNTRY
}


@Parcelize
class UserAddress(private val street: String,
                  private val city: String,
                  private val country: String)
    : Parcelable {


    constructor(): this("","","")

    /** Gets: */
    fun getStreet(): String = street
    fun getCity(): String = city
    fun getCountry(): String = country
    fun getAddress(): String = "$street $city, $country"

    /** Methods: */
}