package com.cofeece.findyourcofeece

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button

import android.widget.EditText
import android.widget.Toast

import android.widget.ViewFlipper
import androidx.activity.viewModels
import androidx.core.view.isEmpty
import androidx.lifecycle.Observer
import com.google.android.material.resources.MaterialResources
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import kotlinx.android.synthetic.main.activity_owner_register.*


private const val TAG = "OwnerRegisterActivity"

class OwnerRegisterActivity : AppCompatActivity() {

    private lateinit var mViewFlipper: ViewFlipper
    private val mViewModel: OwnerRegisterViewModel by viewModels()

    private val mAccountDetailsLayoutHolder by lazy { findViewById<View>(R.id.ownerAccountDetailsLayout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_register)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Observes the editText views with ViewModel object:
        // Observe first the account details views.
        observeAccountDetails()

        // Observe second the restaurant details views.
        observeRestaurantsDetails()

        // Finally, observe the bank details views.
        observeBankDetails()

        // Set the account details, and move on to the restaurant details on the next view in the ViewFlipper.
        ownerRegisterNextPageButton.setOnClickListener {
            if (accountDetailsEntered()) {
                // Check if all fields was filled by user.
                mViewModel.saveAccoutDetails(
                    fullName.text.toString(),
                    userName.text.toString(),
                    email.text.toString(),
                    password.text.toString()
                )

                viewFlipperOwnerRegister.showNext()
            } else {
                Log.d(TAG, "onCreate: user clicked continue before filled all details required")
//                Toast.makeText(this, "Please fill all details.", Toast.LENGTH_LONG).show()
                // Disable error attribute of the inputLayout widget when the editText is not empty anymore.
                disableError()
            }
        }

        // Set the restaurant details, and move on to the bank details on the next view in the ViewFlipper.
        continueToBankBtn.setOnClickListener {
            if (restaurantDetailsEntered()) {
                // Check if all fields was filled by user.
                mViewModel.saveRestaurantDetails(
                    restaurantName.text.toString(),
                    ownerStreet.text.toString(),
                    ownerCity.text.toString(),
                    ownerCountry.text.toString()
                )

                viewFlipperOwnerRegister.showNext()
            } else {
                Log.d(TAG, "onCreate: user clicked continue before filled all details required")
//                Toast.makeText(this, "Please fill all details.", Toast.LENGTH_LONG).show()
                // Disable error attribute of the inputLayout widget when the editText is not empty anymore.
                disableError()

            }
        }

        // Set the bank details, finish registration.
        finishBtn.setOnClickListener {
            if (bankDetailsEntered()) {
                mViewModel.saveBankDetails(
                    ownerBankName.text.toString(),
                    ownerAccountNumber.text.toString(),
                    ownerBankBranch.text.toString()
                )

                mViewModel.insertOwnerToDatabase()
                // TODO: Set intent, and start new activity.
            } else {
                Log.d(TAG, "onCreate: user clicked finish before filled all details required")
//                Toast.makeText(this, "Please fill all details.", Toast.LENGTH_LONG).show()
                // Disable error attribute of the inputLayout widget when the editText is not empty anymore.
                disableError()
            }
        }


        // TODO: Set an onClick method when the back button clicked.
    }

    override fun onSupportNavigateUp(): Boolean {
        return true
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_owner_register, menu)
//        return true
//    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected: starts")
        when (item.itemId) {
            android.R.id.home -> {
                Log.d(TAG, "onOptionsItemSelected: home button clicked")
                if (viewFlipperOwnerRegister.currentView == mAccountDetailsLayoutHolder) {
                    // If the user wants to go back to the main activity(maps activtiy). Wr should let him.
                    Log.d(
                        TAG,
                        "onOptionsItemSelected: current view is ${viewFlipperOwnerRegister.currentView.id.toString()}"
                    )
                    val intent = Intent(this, OwnerHomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d(
                        TAG,
                        "onOptionsItemSelected: current view is ${viewFlipperOwnerRegister.currentView.id.toString()}"
                    )
                    // The user wants to go back to the previous view in the ViewFlipper.
                    viewFlipperOwnerRegister.showPrevious()
                }
            }
        }

        Log.d(TAG, "onOptionsItemSelected: returns ${super.onOptionsItemSelected(item)}")
        return super.onOptionsItemSelected(item)
    }


    private fun observeAccountDetails() {
        mViewModel.ownerName.observe(this, Observer { ownerName ->
            fullName.setText(ownerName)
        })

        mViewModel.ownerUserName.observe(this, Observer { ownerUserName ->
            userName.setText(ownerUserName)
        })

        mViewModel.ownerEmail.observe(this, Observer { ownerEmail ->
            email.setText(ownerEmail)
        })

        mViewModel.ownerPassword.observe(this, Observer { ownerPassword ->
            password.setText(ownerPassword)
        })
    }

    private fun observeRestaurantsDetails() {
        mViewModel.ownerRestaurantName.observe(this, Observer { name ->
            restaurantName.setText(name)
        })

        mViewModel.ownerStreet.observe(this, Observer { street ->
            ownerStreet.setText(street)
        })

        mViewModel.ownerCity.observe(this, Observer { city ->
            ownerCity.setText(city)
        })

        mViewModel.ownerCountry.observe(this, Observer { country ->
            ownerCountry.setText(country)
        })
    }

    private fun observeBankDetails() {
        mViewModel.ownerBankName.observe(this, Observer { name ->
            ownerBankName.setText(name)
        })

        mViewModel.ownerAccountNumber.observe(this, Observer { accountNumber ->
            ownerAccountNumber.setText(accountNumber)
        })

        mViewModel.ownerBranch.observe(this, Observer { branch ->
            ownerBankBranch.setText(branch)
        })
    }



    private fun noDetailUser(ownerDetail: TextInputEditText): Boolean =
        (ownerDetail.text.isNullOrEmpty() || ownerDetail.text.isNullOrBlank())

    private fun setAccountDetailsError() {

    }


    private fun accountDetailsEntered(): Boolean {
        when {
            noDetailUser(fullName) -> {
                Log.d(TAG, "onCreate: Email wasn't entered")
//                fullName.error = "Please enter a ${fullName.hint}"
                fullNameInputLayout.error = "Please enter a ${fullName.hint}"
                return false
            }

            noDetailUser(userName) -> {
                Log.d(TAG, "onCreate: Name wasn't entered")
                usernameInputLayout.error = "Please enter a ${userName.hint}"
                return false
            }

            noDetailUser(email) -> {
                Log.d(TAG, "onCreate: Username wasn't entered")
                emailInputLayout.error = "Please enter a ${email.hint}"
                return false
            }

            noDetailUser(password) -> {
                Log.d(TAG, "onCreate: Password wasn't entered")
                passwordInputLayout.error = "Please enter a ${password.hint}"
                return false
            }

            else -> return true
        }
    }


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
        if (!noDetailUser(fullName)) {
            fullNameInputLayout.isErrorEnabled = false
        }

        if (!noDetailUser(userName)) {
            usernameInputLayout.isErrorEnabled = false
        }

        if (!noDetailUser(email)) {
            emailInputLayout.isErrorEnabled = false
        }

        if (!noDetailUser(password)) {
            passwordInputLayout.isErrorEnabled = false
        }

        if (!noDetailUser(restaurantName)) {
            restaurantNameInputLayout.isErrorEnabled = false
        }

        if (!noDetailUser(ownerStreet)) {
            streetInputLayout.isErrorEnabled = false
        }

        if (!noDetailUser(ownerCity)) {
            cityInputLayout.isErrorEnabled = false
        }

        if (!noDetailUser(ownerCountry)) {
            countryInputLayout.isErrorEnabled = false
        }

        if (!noDetailUser(ownerBankName)) {
            bankNameInputLayout.isErrorEnabled = false
        }

        if (!noDetailUser(ownerAccountNumber)) {
            accountNumberInputLayout.isErrorEnabled = false
        }

        if(!noDetailUser(ownerBankBranch)) {
            branchInputLayout.isErrorEnabled = false
        }
    }
}
