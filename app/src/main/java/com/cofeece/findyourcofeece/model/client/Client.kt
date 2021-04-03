package com.cofeece.findyourcofeece.model.client

import com.cofeece.findyourcofeece.model.user.User

class Client(name: String, username: String, email: String, password: String) :
    User(name, username, email, password) {
    /** Attributes: */
    private lateinit var mPayment: Payment

    /** Constructors: */
    constructor() : this("", "", "", "")

    /** Gets: */
    fun getClientId(): String = id
    fun getPayment(): Payment = mPayment

    /** Sets: */
    fun setClientId(id: String) {
        this.id = id
    }
    fun setPayment(iCardName: String, iCardNumber: String, iMonth: String, iYear: String, iCVV: String) {
        mPayment = Payment(iCardName, iCardNumber, iMonth, iYear, iCVV)
    }
}