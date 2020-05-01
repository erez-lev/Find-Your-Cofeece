package com.cofeece.findyourcofeece.user

enum class AddressDetails {
    STREET, CITY, COUNTRY
}



class UserAddress(private val street: String,
                  private val city: String,
                  private val country: String) {


    constructor(): this("","","")

    /** Gets: */
    fun getStreet(): String = street
    fun getCity(): String = city
    fun getCountry(): String = country
    fun getAddress(): String = "$street $city, $country"

    /** Methods: */
}