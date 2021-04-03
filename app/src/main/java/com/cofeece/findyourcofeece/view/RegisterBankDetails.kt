package com.cofeece.findyourcofeece.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import com.cofeece.findyourcofeece.R

import com.cofeece.findyourcofeece.firebase.DatabaseManager
import com.cofeece.findyourcofeece.firebase.OWNERS

import com.cofeece.findyourcofeece.model.owner.*

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import kotlinx.android.synthetic.main.activity_register_bank_details.*

private const val TAG = "RegisterBankDetails"

class RegisterBankDetails : AppCompatActivity() {

    /** Properties: */
    private val database = DatabaseManager()

    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_bank_details)

        // Set the restaurant details, and move on to the bank details on the next view in the ViewFlipper.
        val name = intent.getStringExtra(AccountDetails.NAME.toString())
        val username = intent.getStringExtra(AccountDetails.USERNAME.toString())
        val email = intent.getStringExtra(AccountDetails.EMAIL.toString())
        val password = intent.getStringExtra(AccountDetails.PASSWORD.toString())

        val owner = Owner(
            name!!,
            username!!,
            email!!,
            password!!
        )

        val restaurant: Restaurant? =
            intent.getParcelableExtra(RestaurantDetails.RESTAURANT.toString())
        if (restaurant != null) {
            Log.d(TAG, "onCreate: owner's restaurant is ${restaurant}")
            owner.setRestaurant(restaurant)
        }

        checkTextChange()

        // Set the bank details, finish registration.
        finishBtn.setOnClickListener {
            if (bankDetailsEntered()) {
                owner.setBank(
                    Bank(
                        ownerBankName.text.toString(),
                        ownerAccountNumber.text.toString(),
                        ownerBankBranch.text.toString()
                    )
                )

                insertOwnerToDatabase(owner)
                val intent = Intent(this, OwnerMenuActivity::class.java)
                startActivity(intent)
            } else {
                Log.d(TAG, "onCreate: user clicked finish before filled all details required")
//                Toast.makeText(this, "Please fill all details.", Toast.LENGTH_LONG).show()
                // Disable error attribute of the inputLayout widget when the editText is not empty anymore.
                disableError()
            }
        }
    }

    /** Class Methods: */
    private fun insertOwnerToDatabase(owner: Owner) {
        database.writeToDatabase(user = owner, type = OWNERS)
    }

    private fun checkTextChange() {
        val bankDetails = arrayListOf<TextInputEditText>(
            ownerBankName,
            ownerAccountNumber,
            ownerBankBranch
        )

        for (detail in bankDetails) {
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


    private fun bankDetailsEntered(): Boolean {
        when {
            noDetailUser(ownerBankName) -> {
                Log.d(TAG, "onCreate: Email wasn't entered")
                bankNameInputLayout.error = "Please enter a ${ownerBankName.hint}"
                return false
            }
            noDetailUser(ownerAccountNumber) -> {
                Log.d(TAG, "onCreate: Name wasn't entered")
                accountNumberInputLayout.error = "Please enter a ${ownerAccountNumber.hint}"
                return false
            }
            noDetailUser(ownerBankBranch) -> {
                Log.d(TAG, "onCreate: Username wasn't entered")
                branchInputLayout.error = "Please enter a ${ownerBankBranch.hint}"
                return false
            }
            else -> return true
        }
    }

    private fun disableError() {
        val bankDetails = arrayListOf<TextInputEditText>(
            ownerBankName,
            ownerAccountNumber,
            ownerBankBranch
        )

        for (detail in bankDetails) {
            if (!noDetailUser(detail)) {
                (detail.parent.parent as TextInputLayout)
                    .isErrorEnabled = false
            }
        }
    }
}
