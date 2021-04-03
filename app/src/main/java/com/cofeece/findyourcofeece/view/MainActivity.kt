package com.cofeece.findyourcofeece.view


import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import com.cofeece.findyourcofeece.R


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
