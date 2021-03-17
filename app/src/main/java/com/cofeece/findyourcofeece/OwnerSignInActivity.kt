package com.cofeece.findyourcofeece

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_owner_sign_in.*

class OwnerSignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_sign_in)

        doneOwnerSignInBtn.setOnClickListener {
            val intent = Intent(this, OwnerMenuActivity::class.java)
            startActivity(intent)
        }
    }
}
