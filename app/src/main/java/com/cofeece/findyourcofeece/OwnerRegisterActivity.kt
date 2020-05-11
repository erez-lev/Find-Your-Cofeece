package com.cofeece.findyourcofeece

import android.accounts.Account
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log

import androidx.core.widget.addTextChangedListener
import com.cofeece.findyourcofeece.owner.AccountDetails
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import kotlinx.android.synthetic.main.register_account_details.*

private const val TAG = "OwnerRegisterActivity"

class OwnerRegisterActivity : AppCompatActivity() {

    /** Properties: */
    private val mDetailList by lazy {
        arrayListOf<TextInputEditText>(
            fullName,
            userName,
            email,
            password
        )
    }

    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_register)

        // Checks wherever the text is empty and should fill it with error or clear the error.
        checkTextChange()

        // Set the account details, and move on to the restaurant details on the next view in the ViewFlipper.
        ownerRegisterNextPageButton.setOnClickListener {
            if (accountDetailsEntered()) {
                // Check if all fields was filled by user.
                val intent = Intent(this, RegisterRestaurantDetails::class.java)
                // Send account details to the restaurant registration activity.
                intent.putExtra(AccountDetails.NAME.toString(), fullName.text.toString())
                intent.putExtra(AccountDetails.USERNAME.toString(), userName.text.toString())
                intent.putExtra(AccountDetails.EMAIL.toString(), email.text.toString())
                intent.putExtra(AccountDetails.PASSWORD.toString(), password.text.toString())
                startActivity(intent)
            } else {
                Log.d(TAG, "onCreate: user clicked continue before filled all details required")
//                Toast.makeText(this, "Please fill all details.", Toast.LENGTH_LONG).show()
                // Disable error attribute of the inputLayout widget when the editText is not empty anymore.
                disableError()
            }
        }
    }

    /** Class Methods: */
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


    private fun disableError() {
        for (detail in mDetailList) {
            if (!noDetailUser(detail)) {
                (detail.parent.parent as TextInputLayout)
                    .isErrorEnabled = false
            }
        }
    }
}
