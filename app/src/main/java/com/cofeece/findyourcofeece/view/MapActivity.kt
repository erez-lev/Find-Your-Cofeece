package com.cofeece.findyourcofeece.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cofeece.findyourcofeece.R
import com.cofeece.findyourcofeece.firebase.AuthenticationManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_map.*

/** Constants: */
private const val TAG = "MapActivity"

class MapActivity : AppCompatActivity() {
    companion object {
        var mCurrentUser: FirebaseUser? = null
    }

    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
    }

    override fun onStart() {
        super.onStart()
        // Initializing current user.
        mCurrentUser = AuthenticationManager().mAuth.currentUser
        Log.d(TAG, "onStart: current user is $mCurrentUser")
    }
}