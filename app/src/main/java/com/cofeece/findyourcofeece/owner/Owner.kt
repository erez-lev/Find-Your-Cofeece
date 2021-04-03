package com.cofeece.findyourcofeece.owner

import com.cofeece.findyourcofeece.user.User

/** Enums: */
enum class OwnerChildren {
    ACCOUNT, RESTAURANT, BANK, STANDS, RATES
}

enum class AccountDetails {
    NAME, USERNAME, EMAIL, PASSWORD
}

private const val TAG = "Owner"

class Owner(
    name: String,
    username: String,
    email: String,
    password: String
) : User(name, username, email, password) {

    /** Properties: */
    private lateinit var restaurant: Restaurant
    private lateinit var bank: Bank

    companion object {
        var creationSucceed = false
    }

    /** Constructors: */
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
//        this.restaurant.setName(restaurant.getName())
//        this.restaurant.setAddress(restaurant.getAddress())
//        this.restaurant.setLatLng(restaurant.getLatLng())
//        this.restaurant.setAddress(restaurant.getAddress())
    }

    fun setBank(bank: Bank) {
        this.bank = bank
    }

    override fun equals(owner: Any?): Boolean {
        val ownerToCompare = owner as Owner
        return this.id == ownerToCompare.id &&
               this.restaurant == ownerToCompare.restaurant
    }
}
