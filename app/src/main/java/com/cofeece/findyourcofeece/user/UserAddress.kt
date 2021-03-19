package com.cofeece.findyourcofeece.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/** Enums: */
enum class AddressDetails {
    STREET, CITY, COUNTRY
}

@Parcelize
class UserAddress(
    private var street: String,
    private var city: String,
    private var country: String
) : Parcelable {

    /** Constructors: */
    constructor(): this("","","")

    /** Gets: */
    fun getStreet(): String = street
    fun getCity(): String = city
    fun getCountry(): String = country
    fun getAddress(): String = "$street $city, $country"

    /** Sets: */
    fun setStreet(iStreet: String) {
        this.street = iStreet
    }
    fun setCity(iCity: String) {
        this.city = iCity
    }
    fun setCountry(iCountry: String) {
        this.country = iCountry
    }
    fun setAddress(iStreet: String, iCity: String, iCountry: String) {
        setStreet(iStreet)
        setCity(iCity)
        setCountry(iCountry)
    }

    override fun toString(): String {
        return "$street $city, $country"
    }

    override fun equals(address: Any?): Boolean {
        val addressToCompare = address as UserAddress
        return this.country == addressToCompare.country &&
               this.city == addressToCompare.city &&
               this.street == addressToCompare.street
    }
}
