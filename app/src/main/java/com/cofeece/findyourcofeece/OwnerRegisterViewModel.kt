package com.cofeece.findyourcofeece

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cofeece.findyourcofeece.firebase.DatabaseManager
import com.cofeece.findyourcofeece.firebase.OWNERS
import com.cofeece.findyourcofeece.owner.Bank
import com.cofeece.findyourcofeece.owner.Owner
import com.cofeece.findyourcofeece.owner.Restaurant
import com.cofeece.findyourcofeece.user.UserAddress
import kotlinx.coroutines.launch

class OwnerRegisterViewModel : ViewModel() {

    val db = DatabaseManager()

    /** Account Details Properties: */
    private val name = MutableLiveData<String>()
    val ownerName: LiveData<String>
        get() = name

    private val username = MutableLiveData<String>()
    val ownerUserName: LiveData<String>
        get() = username

    private val email = MutableLiveData<String>()
    val ownerEmail: LiveData<String>
        get() = email

    private val password = MutableLiveData<String>()
    val ownerPassword: LiveData<String>
        get() = password

    /** Restaurants Details Properties: */
    private val restaurantName = MutableLiveData<String>()
    val ownerRestaurantName: LiveData<String>
        get() = restaurantName

    private val street = MutableLiveData<String>()
    val ownerStreet: LiveData<String>
        get() = street

    private val city = MutableLiveData<String>()
    val ownerCity: LiveData<String>
        get() = city

    private val country = MutableLiveData<String>()
    val ownerCountry: LiveData<String>
        get() = country


    /** Bank Details Properties: */
    private val bankName = MutableLiveData<String>()
    val ownerBankName: LiveData<String>
        get() = bankName

    private val accountNumber = MutableLiveData<String>()
    val ownerAccountNumber: LiveData<String>
        get() = accountNumber

    private val branch = MutableLiveData<String>()
    val ownerBranch: LiveData<String>
        get() = branch


    /** Methods: */
    fun saveAccoutDetails(name: String, username: String, email: String, password: String) {
        this.name.value = name
        this.username.value = username
        this.email.value = email
        this.password.value = password
    }

    fun saveRestaurantDetails(name: String, street: String, city: String, country: String) {
        this.restaurantName.value = name
        this.street.value = street
        this.city.value = city
        this.country.value = country
    }

    fun saveBankDetails(bankName: String, accountNumber: String, branch: String) {
        this.bankName.value = bankName
        this.accountNumber.value = accountNumber
        this.branch.value = branch
    }

    fun signUpAsOwner(): Owner {
        val owner = Owner(
            ownerName.value.toString(),
            ownerUserName.value.toString(),
            ownerEmail.value.toString(),
            ownerPassword.value.toString()
        )

        owner.setRestaurant(
            Restaurant(
                ownerRestaurantName.value.toString(),
                UserAddress(
                    ownerStreet.value.toString(),
                    ownerCity.value.toString(),
                    ownerCountry.value.toString()
                )
            )
        )

        owner.setBank(
            Bank(
                ownerBankName.value.toString(),
                ownerAccountNumber.value.toString(),
                ownerBranch.value.toString()
            )
        )

        Log.d("ViewModel", "signUp: owner's name is ${owner.name}")

        return owner
    }

    fun insertOwnerToDatabase() {
//        viewModelScope.launch {
            db.writeToDatabase(user = signUpAsOwner(), type = OWNERS)
//        }
    }

    fun isEmptyField() {

    }
}