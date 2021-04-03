package com.cofeece.findyourcofeece.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cofeece.findyourcofeece.R
import com.cofeece.findyourcofeece.firebase.AuthenticationManager
import kotlinx.android.synthetic.main.activity_client_home.*

private const val TAG = "ClientHomeActivity"

class ClientHomeActivity : AppCompatActivity() {
    private val mAuth = AuthenticationManager().mAuth

    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_home)

        clientSignUp.setOnClickListener {
            val intent = Intent(this, ClientRegisterActivity::class.java)
            startActivity(intent)
        }

        clientSignIn.setOnClickListener {
            val intent = Intent(this, ClientMenuActivity::class.java)
            startActivity(intent)
//            if (mAuth.currentUser == null) {
//                // User need to register first.
//                Log.d(TAG, "onCreate: current user is ${mAuth.currentUser}")
//            } else {
//                Log.d(TAG, "onCreate: current user is ${mAuth.currentUser}")
//                val intent = Intent(this, ClientMenuActivity::class.java)
//                startActivity(intent)
//            }
        }
    }
}
