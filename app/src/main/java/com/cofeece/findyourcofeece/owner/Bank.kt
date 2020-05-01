package com.cofeece.findyourcofeece.owner


enum class BankDetails {
    NAME, ACCOUNT, BRANCH
}


class Bank(private var name: String, private var account: String, private var branch: String) {

    constructor(): this("","","")

    /** Gets: */
    fun getName(): String = name
    fun getAccount(): String = account
    fun getBranch(): String = branch


    /** Sets: */
    fun setName(name: String) {
        this.name = name
    }

}