package com.cofeece.findyourcofeece.model.user

private const val TAG = "User"

internal const val NAME = "Name"
internal const val USERNAME = "UserName"
internal const val EMAIL = "Email"
internal const val PASSWORD = "Password"

open class User(
    var name: String,
    var username: String,
    var email: String,
    var password: String
) {
    /** Properties: */
    protected var id: String = ""
}