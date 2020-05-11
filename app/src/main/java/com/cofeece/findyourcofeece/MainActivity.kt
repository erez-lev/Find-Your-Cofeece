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

class MainActivity :
    AppCompatActivity() {

    companion object {
        var mCurrentUser: FirebaseUser? = null
    }
    var mOwners = ArrayList<Owner>()
    private var mDatabase = DatabaseManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ownerSideButton.setOnClickListener {
            Snackbar.make(
                it,
                "For going to owner's side, please click a long tap.",
                Snackbar.LENGTH_LONG
            ).setAnchorView(it)
                .show()
        }

        ownerSideButton.setOnLongClickListener {
            val intent = Intent(this, OwnerHomeActivity::class.java)
            startActivity(intent)
            true
        }
    }

    override fun onStart() {
        super.onStart()

        // Initializing current user.
        mCurrentUser = AuthenticationManager().mAuth.currentUser
        Log.d(TAG, "onStart: current user is $mCurrentUser")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

//            R.id.viewRestaurantList -> // TODO: show the restaurants list, the default in 5 km.

//            R.id.fiveKmRestaurantsDistance -> //TODO: show the restaurants which are 5km distance in the list.

//            R.id.tenKmRestaurantsDistance -> //TODO: show the restaurants which are 10km distance in the list.

        }
        return super.onOptionsItemSelected(item)
    }

}
