package com.cofeece.findyourcofeece.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object FirebaseManager {
    /** Properties: */
    val database = FirebaseDatabase.getInstance()
    val authentication = FirebaseAuth.getInstance()
}