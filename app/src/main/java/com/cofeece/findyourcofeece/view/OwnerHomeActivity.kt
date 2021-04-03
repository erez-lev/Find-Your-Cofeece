package com.cofeece.findyourcofeece.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cofeece.findyourcofeece.R
import kotlinx.android.synthetic.main.activity_owner_home.*

class OwnerHomeActivity : AppCompatActivity() {
    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_home)

        ownerSignIn.setOnClickListener {
            val goToLoginPage = Intent(this, OwnerMenuActivity::class.java)
            startActivity(goToLoginPage)
        }

        ownerSignUp.setOnClickListener {
            val goToRegisterOwnerPage = Intent(this, OwnerRegisterActivity::class.java)
            startActivity(goToRegisterOwnerPage)
        }
    }
}
