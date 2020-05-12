package com.cofeece.findyourcofeece

import android.app.Activity

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager

import android.graphics.Bitmap
import android.graphics.Canvas

import android.location.Address
import android.location.Geocoder

import android.os.Bundle

import android.util.Log

import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

import androidx.fragment.app.activityViewModels
import com.cofeece.findyourcofeece.owner.Owner
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_map.*

import java.io.IOException
import java.util.*

private const val TAG = "MapFragment"

/** Constants: */
const val REQUEST_CHECK_SETTINGS = 43

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_MAP = "Map"
private const val ARG_LOCATION = "Location"

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class MapFragment : Fragment(),
    OnMapReadyCallback,
    PermissionListener {

    /** Properties: */
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val mViewModel: MapsViewModel by activityViewModels()
    private var mContext: Context? = null
    private var mActivity: FragmentActivity? = null
    private lateinit var mMarker: BitmapDescriptor

    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)

        // Initialization:
        mContext = this.context
        mActivity = this.activity
        try {
            mFusedLocationClient = FusedLocationProviderClient(mContext!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: called")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: called")
        super.onViewCreated(view, savedInstanceState)

        try {
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Set markers on the map. The owner's restaurants.
//        setRestaurantsOnMap()
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        Log.d(TAG, "onMapReady: called")
        mMap = googleMap ?: return

        if (MainActivity.mCurrentUser == null) {
            Log.d(TAG, "onMapReady: current user is ${MainActivity.mCurrentUser}")
            // The reserve button can not be visible for unregistered users.
            if (reserveButton != null) {
                reserveButton.visibility = View.GONE
            } else {
                Log.d(TAG, "onMapReady: reserve button wasn't created yet.")
            }
            // Send the user for registration when he doesn't have an account yet.
            mMap.setOnMapClickListener {
                val intent = Intent(mContext, ClientHomeActivity::class.java)
                startActivity(intent)
            }
        } else {
            // User can access functionality in the main activity.
            reserveButton.visibility = View.VISIBLE
        }

        if (isPermissionGiven()) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            googleMap.uiSettings.isZoomControlsEnabled = true
            getCurrentLocation()

            // Set map style.
            mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this.activity, R.raw.style_json
                )
            )
//            setRestaurantsOnMap()
        } else {
            givePermission()
        }
    }

    /** Class Methods: */
    private fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
        Log.d(TAG, "getLocationFromAddress: called")
        val coder = Geocoder(context)
        val address: List<android.location.Address>?
        var p1: LatLng? = null
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location = address[0]
            p1 = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }


    private fun createCustomMarker(): BitmapDescriptor {
        val drawble = ContextCompat.getDrawable(mContext!!, R.drawable.ic_coffee_yellow_24dp)
        if (drawble != null) {
            drawble?.setBounds(0, 0, drawble.intrinsicWidth, drawble.intrinsicHeight)
            val bitmap = Bitmap.createBitmap(
                drawble.intrinsicWidth,
                drawble.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawble.draw(canvas)

            return BitmapDescriptorFactory.fromBitmap(bitmap)
        } else {
            throw NullPointerException("There was no bitmap to create.")
        }

    }

    private fun setRestaurantsOnMap() {
        Log.d(TAG, "setRestaurantsOnMap: called")
        mMarker = createCustomMarker()

        // Set markers on the map. The owner's restaurants.
        mViewModel.loadOwners(object : MapsViewModel.OnDataCallback {
            override fun onDataCallBack(owners: ArrayList<Owner>) {
                Log.d(TAG, "onDataCallBack: called")
                if (owners.isNotEmpty()) {
                    for (owner in owners) {
                        try {
                            val ownerAddress = owner.getRestaurant().getAddress()
                            Log.d(
                                TAG,
                                "setRestaurantsOnMap: owner's address is: ${ownerAddress.getAddress()}"
                            )
                            val latlng =
                                getLocationFromAddress(mContext, ownerAddress.getAddress())
                            Log.d(
                                TAG,
                                "setRestaurantsOnMap: latitude is - ${latlng?.latitude}," +
                                        " longitude is - ${latlng?.longitude} "
                            )
                            if (latlng != null) {
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(latlng)
                                        .title(owner.getRestaurant().getName())
                                        .snippet(
                                            owner.getRestaurant().getAddress().getAddress()
                                        )
                                        .icon(mMarker)
//                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                )

                            } else {
                                Log.d(TAG, "setRestaurantsOnMap: latlng is null")
                            }
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        } catch (e: IndexOutOfBoundsException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        })

    }


    private fun isPermissionGiven(): Boolean {
        return ActivityCompat.checkSelfPermission(
            mContext!!,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun givePermission() {
        Dexter.withActivity(this.activity)
            .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(this)
            .check()
    }


    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        getCurrentLocation()
    }


    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {
        token!!.continuePermissionRequest()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Log.d(TAG, "onPermissionDenied: starts")
        Toast.makeText(mContext, "Permission required for showing location", Toast.LENGTH_LONG)
            .show()

        this.activity?.finish()
        Log.d(TAG, "onPermissionDenied: ends")
    }

    private fun getCurrentLocation() {
        Log.d(TAG, "getCurrentLocation: called")
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (10 * 1000).toLong()
        locationRequest.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        val result =
            LocationServices.getSettingsClient(mActivity as Activity)
                .checkLocationSettings(locationSettingsRequest)
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                if (response!!.locationSettingsStates.isLocationPresent) {
                    getLastLocation()
                }
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS)
                    } catch (e: IntentSender.SendIntentException) {
                    } catch (e: ClassCastException) {
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

    private fun getLastLocation() {
        Log.d(TAG, "getLastLocation: called")
        mFusedLocationClient.lastLocation
            .addOnCompleteListener(mActivity as Activity) { task ->
                if (task.isSuccessful && task.result != null) {
                    val mLastLocation = task.result

                    var address = "No known address"

                    val gcd = Geocoder(mContext, Locale.getDefault())
                    val addresses: List<Address>
                    try {
                        addresses = gcd.getFromLocation(
                            mLastLocation!!.latitude,
                            mLastLocation.longitude,
                            1
                        )
                        if (addresses.isNotEmpty()) {
                            address = addresses[0].getAddressLine(0)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }


                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(mLastLocation!!.latitude, mLastLocation.longitude))
                            .title("Current Location")
                            .snippet(address)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    )

                    val cameraPosition = CameraPosition.Builder()
                        .target(LatLng(mLastLocation.latitude, mLastLocation.longitude))
                        .zoom(17f)
                        .build()
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                } else {
                    Toast.makeText(mContext, "No current location found", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                if (resultCode == Activity.RESULT_OK) {
                    getCurrentLocation()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }
}
