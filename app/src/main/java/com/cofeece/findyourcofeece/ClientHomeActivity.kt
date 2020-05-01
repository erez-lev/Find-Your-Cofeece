package com.cofeece.findyourcofeece

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_client_home.*

class ClientHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_home)

        clientSignUp.setOnClickListener {
            val intent = Intent(this, ClientRegisterActivity::class.java)
            startActivity(intent)
        }

        clientSignIn.setOnClickListener {
            // TODO: start client's log in activity.
        }
    }
}
