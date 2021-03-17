package com.cofeece.findyourcofeece

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.widget.addTextChangedListener

import com.cofeece.findyourcofeece.client.Client
import com.cofeece.findyourcofeece.firebase.AuthenticationManager
import com.cofeece.findyourcofeece.firebase.CLIENTS
import com.cofeece.findyourcofeece.firebase.DatabaseManager
import com.cofeece.findyourcofeece.user.User

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_client_register.*
import kotlin.concurrent.thread

private const val TAG = "ClientRegisterActivity"

class ClientRegisterActivity : AppCompatActivity() {

    /** Properties: */
    private val db = DatabaseManager()
    private val auth = AuthenticationManager()

    lateinit var mCurrent: Client

    private val mDetailList by lazy {
        arrayListOf<TextInputEditText>(
            clientFullName,
            clientUserName,
            clientEmail,
            clientPassword,
            clientCardName,
            clientCardNumber,
            month,
            year,
            CVV
        )
    }


    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_register)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Checks wherever the edit text was filled or not. Prompt with an error if is need to.
        checkTextChange()

        // Set client account details and move to payment.
        clientRegisterCntBtn.setOnClickListener {
            if (accountDetailsEntered()) {
                mCurrent = Client(
                    clientFullName.text.toString(),
                    clientUserName.text.toString(),
                    clientEmail.text.toString(),
                    clientPassword.text.toString()
                )

                Log.d(TAG, "onCreate: current client is $mCurrent")
                clientViewFlipper.showNext()
            } else {
                disableError()
            }
        }

        // Set payment details and register client to database.
        clientFinishBtn.setOnClickListener {
            if (paymentDetailsEntered()) {
                mCurrent.setPayment(
                    clientCardName.text.toString(),
                    clientCardNumber.text.toString(),
                    month.text.toString(),
                    year.text.toString(),
                    CVV.text.toString()
                )
                thread {
                    insertClientToDatabase()
                }.id

                val userAsClient: User = mCurrent
                Log.d(TAG, "Before authentication called, user name is ${userAsClient.name}")
                auth.signUp(userAsClient)

                val intent = Intent(this, ClientHomeActivity::class.java)
                startActivity(intent)
            } else {
                disableError()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected: starts")
        when (item.itemId) {
            android.R.id.home -> {
                Log.d(TAG, "onOptionsItemSelected: home button clicked")
                if (clientViewFlipper.currentView == findViewById(R.id.clientAccountDetailsLayout)) {
                    // If the user wants to go back to the owner home activity. We should let him.
                    Log.d(
                        TAG,
                        "onOptionsItemSelected: current view is ${clientViewFlipper.currentView.id}"
                    )
                    val intent = Intent(this, OwnerHomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d(
                        TAG,
                        "onOptionsItemSelected: current view is ${clientViewFlipper.currentView.id}"
                    )
                    // The user wants to go back to the previous view in the ViewFlipper.
                    clientViewFlipper.showPrevious()
                }
            }
        }

        Log.d(TAG, "onOptionsItemSelected: returns ${super.onOptionsItemSelected(item)}")
        return super.onOptionsItemSelected(item)
    }

    /** Class Methods: */
    private fun insertClientToDatabase() {
        db.writeToDatabase(user = mCurrent, type = CLIENTS)
    }

    private fun checkTextChange() {
        for (detail in mDetailList) {
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


    private fun accountDetailsEntered(): Boolean {
        val accountDetails = arrayListOf<TextInputEditText>(
            clientFullName,
            clientUserName,
            clientEmail,
            clientPassword
        )

        var areEntered = false

        Log.d(TAG, "accountDetailsEntered: at start, are all details entered? $areEntered")
        for (detail in accountDetails) {
            if (noDetailUser(detail)) {
                (detail.parent.parent as TextInputLayout).error = "Please enter a ${detail.hint}"
                return false
            } else {
                areEntered = true
            }
        }
        Log.d(TAG, "accountDetailsEntered: at last, are all details entered? $areEntered")
        return areEntered
    }


    private fun paymentDetailsEntered(): Boolean {
        val paymentDtails = arrayListOf<TextInputEditText>(
            clientCardName,
            clientCardNumber,
            month,
            year,
            CVV
        )

        var areEntered = false

        for (detail in paymentDtails) {
            if (noDetailUser(detail)) {
                if (detail == month
                    || detail == year
                    || detail == CVV
                ) {
                    detail.error = "Please enter a number"
                } else {
                    (detail.parent.parent as TextInputLayout).error =
                        "Please enter a ${detail.hint}"
                }
                return false
            }
            areEntered = true
        }
        return areEntered

    }

    private fun disableError() {
        for (detail in mDetailList) {
            if (!noDetailUser(detail)) {
                (detail.parent.parent as TextInputLayout).isErrorEnabled = false
            }
        }
    }
}
