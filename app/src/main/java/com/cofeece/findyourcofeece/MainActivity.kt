package com.cofeece.findyourcofeece

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.cofeece.findyourcofeece.firebase.AuthenticationManager
import com.cofeece.findyourcofeece.firebase.DatabaseManager
import com.cofeece.findyourcofeece.firebase.OWNERS
import com.cofeece.findyourcofeece.firebase.OwnerListCallBack

import com.cofeece.findyourcofeece.owner.Owner

import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser

import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*

import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    companion object {
        var mCurrentUser: FirebaseUser? = null
    }

    override fun onStart() {
        super.onStart()

        // Initializing current user.
        mCurrentUser = AuthenticationManager().mAuth.currentUser
        Log.d(TAG, "onStart: current user is $mCurrentUser")
    }

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
