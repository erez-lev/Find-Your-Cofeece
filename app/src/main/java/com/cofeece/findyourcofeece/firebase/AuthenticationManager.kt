package com.cofeece.findyourcofeece.firebase

import android.util.Log
import com.cofeece.findyourcofeece.user.User
import com.google.firebase.auth.FirebaseUser

private const val TAG = "AuthenticationManager"

class AuthenticationManager {
    /** Properties: */
    val mAuth = FirebaseManager.authentication

    /** Interfaces: */
    interface AuthenticationCallback {
        fun onCallback(user: User)
    }

    /** Methods: */
    fun signUp(user: User, callback: AuthenticationCallback) {
        callback.onCallback(user)
        mAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(
                        TAG,
                        "signUp: createUserWithEmailAndPassword: registered completed. User is ${mAuth.currentUser}"
                    )
                    val currentUser: FirebaseUser? = mAuth.currentUser
                    Log.d(
                        TAG,
                        "signUp: createUserWithEmailAndPassword: registered completed. User is $currentUser"
                    )
                } else {
                    Log.d(
                        TAG,
                        "signUp: createUserWithEmailAndPassword: registered completed unsecceefully." +
                                " User is ${mAuth.currentUser}"
                    )
                }
            }
    }
}