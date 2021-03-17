package com.cofeece.findyourcofeece


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import com.cofeece.findyourcofeece.client.Client
import com.cofeece.findyourcofeece.firebase.AuthenticationManager
import com.cofeece.findyourcofeece.firebase.DatabaseManager
import com.cofeece.findyourcofeece.owner.Owner
import com.cofeece.findyourcofeece.user.User

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser


import kotlinx.android.synthetic.main.activity_main.*


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ownerSideButton.setOnClickListener {
            val intent = Intent(this, OwnerHomeActivity::class.java)
            startActivity(intent)
        }

        clientSideButton.setOnClickListener {
            val intent = Intent(this, ClientHomeActivity::class.java)
            startActivity(intent)
        }
    }
}
