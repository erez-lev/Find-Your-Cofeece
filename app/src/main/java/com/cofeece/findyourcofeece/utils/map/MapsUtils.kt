package com.cofeece.findyourcofeece.utils.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.core.content.ContextCompat
import com.cofeece.findyourcofeece.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

const val TAG = "MapsUtils"


class MapsUtils {
    fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
        Log.d(TAG, "getLocationFromAddress: called")
        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: LatLng? = null
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 2)
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

    fun createCustomMarker(context: Context?): BitmapDescriptor {
        val drawble = ContextCompat.getDrawable(context!!, R.drawable.ic_coffee_yellow_24dp)
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
}