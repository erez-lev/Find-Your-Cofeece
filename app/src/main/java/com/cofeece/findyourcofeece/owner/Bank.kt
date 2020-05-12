package com.cofeece.findyourcofeece.owner

/** Enums: */
enum class BankDetails {
    NAME, ACCOUNT, BRANCH
}

class Bank(private var name: String, private var account: String, private var branch: String) {

    /** Constructors: */
    constructor() : this("", "", "")

    /** Gets: */
    fun getName(): String = name
    fun getAccount(): String = account
    fun getBranch(): String = branch


    /** Sets: */
    fun setName(name: String) {
        this.name = name
    }
}