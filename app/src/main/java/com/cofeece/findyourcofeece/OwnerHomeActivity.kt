package com.cofeece.findyourcofeece

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class OwnerHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_home)

//        val signIn = findViewById<Button>(R.id.ownerSignIn)
//        signIn.setOnClickListener {
//            val goToLoginPage = Intent(this, OwnerLoginActivity::class.java)
//            startActivity(goToLoginPage)
//        }

        val signUp = findViewById<Button>(R.id.ownerSignUp)
        signUp.setOnClickListener {
            val goToRegisterOwnerPage = Intent(this, OwnerRegisterActivity::class.java)
            startActivity(goToRegisterOwnerPage)
        }
    }
}
