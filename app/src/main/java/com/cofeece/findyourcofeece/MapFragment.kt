package com.cofeece.findyourcofeece

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.cofeece.findyourcofeece.firebase.DatabaseManager
import com.cofeece.findyourcofeece.firebase.OWNERS
import com.cofeece.findyourcofeece.map.DirectionsJSONParser
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
import kotlinx.android.synthetic.main.fragment_map.*

import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


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

private interface OnMapFinished {
    fun onMapFinished()
}

class MapFragment :
    Fragment(),
    OnMapReadyCallback,
    PermissionListener,
    OnMapFinished {

    /** Properties: */
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var mDB = DatabaseManager()
    private val mViewModel: MapsViewModel by activityViewModels()
    private var mContext: Context? = null
    private var mActivity: FragmentActivity? = null
    private lateinit var mMarker: BitmapDescriptor
    private var mIsOnMapReadyCalled: Boolean = false
    private lateinit var mCurrentLatLng: LatLng
    private lateinit var mDestinationLatLng: LatLng


    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)

        // Initialization:
        mContext = this.context
        mActivity = this.activity
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mViewModel.loadOwnersTest()
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
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        Log.d(TAG, "onMapReady: called")
        mMap = googleMap ?: return

        if (ActivityCompat.checkSelfPermission(
                mContext!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            mMap!!.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = false
            googleMap.uiSettings.isZoomControlsEnabled = false
            getCurrentLocation()
        } else {
            givePermission()
        }

        this.onMapFinished()
        // TODO: Need to add custom map.
    }

    override fun onMapFinished() {
        Log.d(TAG, "onMapFinished: called")
//        setRestaurantsOnMap()
        setRestaurantsOnMapVM()
        mMap.setOnMarkerClickListener {
            mDestinationLatLng = it.position
            reserveButton.visibility = View.VISIBLE
            reserveButton.isEnabled = true
            reserveButton.setOnClickListener {
                getDirectionsByPolyline()
            }
            false
        }
        mMap.setOnMapClickListener {
            reserveButton.visibility = View.GONE
            reserveButton.isEnabled = false
        }
    }

    private fun getDirectionsByPolyline() {
            Log.d(TAG, "getDirectionsByPolyline: called")
            // Checks, whether start and end locations are captured
            // Getting URL to the Google Directions API
            // Checks, whether start and end locations are captured
            // Getting URL to the Google Directions API
            val url = getDirectionsUrl(mCurrentLatLng, mDestinationLatLng)
            Log.d("url", url + "")
            val downloadTask = DownloadTask()
            // Start downloading json data from Google Directions API
            // Start downloading json data from Google Directions API
            downloadTask.execute(url)
    }

    override fun onStart() {
        Log.d(TAG, "onStart: called")
        super.onStart()
    }

    override fun onStop() {
        Log.d(TAG, "onStop: called")
        super.onStop()
        mViewModel.clearOwnerList()
    }

    /** Class Methods: */
    private fun setRestaurantsOnMapVM() {
        Log.d(TAG, "setRestaurantsOnMapVM: called")
        mMarker = createCustomMarker()
        mViewModel.ownersData.observe(this, { owners ->
            Log.d(TAG, "setRestaurantsOnMapVM: in mViewModel.loadOwners owners are $owners")
            owners.forEach { owner ->
                val ownerFullAddress = owner.getRestaurant().getAddress().toString()
                val latLng: LatLng? = getLocationFromAddress(mContext, ownerFullAddress)

                if (latLng != null) {
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(owner.getRestaurant().getName())
                            .snippet(owner.getRestaurant().getAddress().toString())
                            .icon(mMarker)
                    )
                } else {
                    Log.d(TAG, "setRestaurantsOnMapVM: Error! latlng of the current address is wrong")
                }
            }
        })

        Log.d(TAG, "setRestaurantsOnMapVM: ends")
    }

//    private fun setRestaurantsOnMap() {
//        Log.d(TAG, "setRestaurantsOnMap: called")
//        mMarker = createCustomMarker()
//
//        // Set markers on the map. The owner's restaurants.
//        mDB.readFromDatabase(OWNERS, object : DatabaseManager.OnDataCallBack {
//            override fun onOwnerDataCallBack(owners: ArrayList<Owner>) {
//                Log.d(TAG, "onDataCallBack: called")
//                if (owners.isNotEmpty()) {
//                    for (owner in owners) {
//                        try {
//                            val ownerAddress = owner.getRestaurant().getAddress()
//                            Log.d(
//                                TAG,
//                                "setRestaurantsOnMap: owner's address is: ${ownerAddress.getAddress()}"
//                            )
//                            val latlng =
//                                getLocationFromAddress(mContext, ownerAddress.getAddress())
//                            Log.d(
//                                TAG,
//                                "setRestaurantsOnMap: latitude is - ${latlng?.latitude}," +
//                                        " longitude is - ${latlng?.longitude} "
//                            )
//                            if (latlng != null) {
//                                mMap.addMarker(
//                                    MarkerOptions()
//                                        .position(latlng)
//                                        .title(owner.getRestaurant().getName())
//                                        .snippet(
//                                            owner.getRestaurant().getAddress().getAddress()
//                                        )
//                                        .icon(mMarker)
////                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
//                                )
//
//                            } else {
//                                Log.d(TAG, "setRestaurantsOnMap: latlng is null")
//                            }
//                        } catch (e: NullPointerException) {
//                            e.printStackTrace()
//                        } catch (e: IndexOutOfBoundsException) {
//                            e.printStackTrace()
//                        }
//                    }
//                }
//            }
//        })
//    }

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
        if (ActivityCompat.checkSelfPermission(
                mContext!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
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

                        mCurrentLatLng = LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude)

                        mMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(mLastLocation!!.latitude, mLastLocation.longitude))
                                .title("Current Location")
                                .snippet(address)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        )

                        val cameraPosition = CameraPosition.Builder()
                            .target(LatLng(mLastLocation.latitude, mLastLocation.longitude))
                            .zoom(15f)
                            .build()
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    } else {
                        Toast.makeText(mContext, "No current location found", Toast.LENGTH_LONG)
                            .show()
                    }
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

    private inner class DownloadTask :
        AsyncTask<String?, Void?, String>() {
        override fun doInBackground(vararg url: String?): String {
            var data = ""
            if (url[0] == null) {
                Log.d(TAG, "Got empty url")
            }

            try {
                data = downloadUrl(url[0]!!) ?: ""
            } catch (e: java.lang.Exception) {
                Log.d("Background Task", e.toString())
            }

            Log.d(TAG, "DownloadTask: doInBackground: data is $data")
            return data
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            Log.d(TAG, "DownloadTask: onPostExecute: result is $result")
            val parserTask = ParserTask()
            parserTask.execute(result)
        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private inner class ParserTask :
        AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>() {

        val map = MapFragment::mMap

        // Parsing the data in non-ui thread
        override fun doInBackground(vararg jsonData: String?): List<List<HashMap<String, String>>>? {
            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null
            try {
                Log.d(TAG, "ParserTask: onPostExecute: jsonData is ${jsonData[0]}")
                jObject = JSONObject(jsonData[0]!!)
                val parser = DirectionsJSONParser()
                routes = parser.parse(jObject)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return routes
        }

        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
//            progressDialog.dismiss()
            Log.d(TAG, "ParserTask: onPostExecute: result is $result")
            var points: ArrayList<LatLng> = ArrayList()
            var lineOptions: PolylineOptions
            for (i in result!!.indices) {
                val path = result[i]
                for (j in path.indices) {
                    val point = path[j]
                    val lat = point["lat"]!!.toDouble()
                    val lng = point["lng"]!!.toDouble()
                    val position = LatLng(lat, lng)
                    points.add(position)
                }
            }

            lineOptions = PolylineOptions()
                .addAll(points)
                .width(12f)
                .color(Color.BLACK)
                .geodesic(true)

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions)
        }
    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String? {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude

        // Sensor enabled
        val sensor = "sensor=false"
        val mode = "mode=driving"
        val apiKey = "key=MY_API_KEY" // Need to replace "MY_API_KEY" with my real api key from google, for this to work.
        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor&$mode&$apiKey"

        // Output format
        val output = "json"

        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }

    /**
     * A method to download json data from url
     */
    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String? {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            iStream = urlConnection!!.inputStream
            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line: String? = ""
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            data = sb.toString()
            br.close()
            Log.d("data", data)
        } catch (e: java.lang.Exception) {
            Log.d("Exception", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() =
            MapFragment().apply {
                return MapFragment()
            }

        @Throws(IOException::class)
        fun downloadUrl(strUrl: String): String? {
            var data = ""
            var iStream: InputStream? = null
            var urlConnection: HttpURLConnection? = null
            try {
                val url = URL(strUrl)
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connect()
                iStream = urlConnection!!.inputStream
                val br = BufferedReader(InputStreamReader(iStream))
                val sb = StringBuffer()
                var line: String? = ""
                while (br.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                data = sb.toString()
                br.close()
                Log.d("data", data)
            } catch (e: java.lang.Exception) {
                Log.d("Exception", e.toString())
            } finally {
                iStream!!.close()
                urlConnection!!.disconnect()
            }
            return data
        }
    }
}
