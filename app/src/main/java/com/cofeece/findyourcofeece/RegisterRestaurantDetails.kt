package com.cofeece.findyourcofeece

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.core.widget.addTextChangedListener

import com.cofeece.findyourcofeece.owner.AccountDetails
import com.cofeece.findyourcofeece.owner.Restaurant
import com.cofeece.findyourcofeece.owner.RestaurantDetails

import com.cofeece.findyourcofeece.user.UserAddress
import com.google.android.gms.maps.model.LatLng

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import kotlinx.android.synthetic.main.activity_register_restaurant_details.*
import java.io.IOException

private const val TAG = "RegisterRestaurantDetai"

class RegisterRestaurantDetails : AppCompatActivity() {

    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_restaurant_details)

        // Set the restaurant details, and move on to the bank details on the next view in the ViewFlipper.
        val name = intent.getStringExtra(AccountDetails.NAME.toString())
        val username = intent.getStringExtra(AccountDetails.USERNAME.toString())
        val email = intent.getStringExtra(AccountDetails.EMAIL.toString())
        val password = intent.getStringExtra(AccountDetails.PASSWORD.toString())

        checkTextChange()

        continueToBankBtn.setOnClickListener {
            if (restaurantDetailsEntered()) {
                // Check if all fields was filled by user.
                val address = UserAddress(
                    ownerStreet.text.toString(),
                    ownerCity.text.toString(),
                    ownerCountry.text.toString()
                )

                val restaurant = Restaurant(restaurantName.text.toString(), address)
                Log.d(TAG, "onCreate: restaurant address is $address")
//                val position = getLocationFromAddress(this, address.toString())
//                restaurant.setLatLng(position)
//                Log.d(TAG, "onCreate: restaurant latlng is ${restaurant.getLatLng()}")

                val intent = Intent(this, RegisterBankDetails::class.java)
                intent.putExtra(AccountDetails.NAME.toString(), name)
                intent.putExtra(AccountDetails.USERNAME.toString(), username)
                intent.putExtra(AccountDetails.EMAIL.toString(), email)
                intent.putExtra(AccountDetails.PASSWORD.toString(), password)

                intent.putExtra(RestaurantDetails.RESTAURANT.toString(), restaurant)
                startActivity(intent)
            } else {
                Log.d(TAG, "onCreate: user clicked continue before filled all details required")
                // Disable error attribute of the inputLayout widget when the editText is not empty anymore.
                disableError()
            }
        }
    }

    /** Class Methods: */
    private fun checkTextChange() {
        val detailList = arrayListOf<TextInputEditText>(
            restaurantName,
            ownerStreet,
            ownerCity,
            ownerCountry
        )

        for (detail in detailList) {
            detail.addTextChangedListener {
                if (it.isNullOrBlank() || it.isNullOrEmpty()) {
                    (detail.parent.parent as TextInputLayout)
                        .error = "Please enter a ${detail.hint}"
                } else {
                    (detail.parent.parent as TextInputLayout)
                        .isErrorEnabled = false
                }
            }
        }
    }

    private fun noDetailUser(ownerDetail: TextInputEditText): Boolean =
        (ownerDetail.text.isNullOrEmpty() || ownerDetail.text.isNullOrBlank())

    private fun restaurantDetailsEntered(): Boolean {
        when {
            noDetailUser(restaurantName) -> {
                Log.d(TAG, "onCreate: Email wasn't entered")
                restaurantNameInputLayout.error = "Please enter a ${restaurantName.hint}"
                return false
            }
            noDetailUser(ownerStreet) -> {
                Log.d(TAG, "onCreate: Email wasn't entered")
                streetInputLayout.error = "Please enter a ${ownerStreet.hint}"
                return false
            }
            noDetailUser(ownerCity) -> {
                Log.d(TAG, "onCreate: Name wasn't entered")
                cityInputLayout.error = "Please enter a ${ownerCity.hint}"
                return false
            }
            noDetailUser(ownerCountry) -> {
                Log.d(TAG, "onCreate: Username wasn't entered")
                countryInputLayout.error = "Please enter a ${ownerCountry.hint}"
                return false
            }
            else -> return true
        }
    }

    private fun disableError() {
        val detailList = arrayListOf<TextInputEditText>(
            restaurantName,
            ownerStreet,
            ownerCity,
            ownerCountry
        )

        for (detail in detailList) {
            if (!noDetailUser(detail)) {
                (detail.parent.parent as TextInputLayout)
                    .isErrorEnabled = false
            }
        }
    }

    fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
        Log.d(com.cofeece.findyourcofeece.map.TAG, "getLocationFromAddress: called")
        Log.d(com.cofeece.findyourcofeece.map.TAG, "getLocationFromAddress: inside thread starts")
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
}
