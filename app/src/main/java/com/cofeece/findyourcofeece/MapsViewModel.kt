package com.cofeece.findyourcofeece

import android.util.Log
import androidx.lifecycle.*

import com.cofeece.findyourcofeece.firebase.DatabaseManager

import com.cofeece.findyourcofeece.owner.Owner
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.ValueEventListener

import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "MapsViewModel"

/** Constants: */
val EMPTY_OWNER_LIST: List<Owner> = Collections.emptyList()

class MapsViewModel : ViewModel() {

    /** Properties: */
    private val db = DatabaseManager()
    private val owners = MutableLiveData<List<Owner>>()
    val ownerList: LiveData<List<Owner>>
        get() = owners

    /** Interfaces: */
    interface OnDataCallback {
        fun onDataCallBack(values: ArrayList<Owner>)
    }

    /** Methods: */
    init {
        Log.d(TAG, "init: called")
        owners.postValue(EMPTY_OWNER_LIST)
    }


    fun setOwnerList(owners: ArrayList<Owner>) {
        Log.d(TAG, "setOwnerList: called")

        this.owners.postValue(owners)
        Log.d(TAG, "loadOwners: onDataChanged: owners value is ${this.owners.value}")
    }


    fun loadOwners(callback: OnDataCallback) {
        Log.d(TAG, "loadOwners: starts")
        val ownerList = ArrayList<Owner>()

        db.ownerRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: called")
                for (snapshot in dataSnapshot.children) {
                    val owner = snapshot.getValue(Owner::class.java)
                    if (owner != null) {
                        ownerList.add(owner)
                    } else {
                        Log.d(TAG, "loadOwners: onDataChanged: owner is $owner")
                    }
                }
                callback.onDataCallBack(ownerList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "onCancelled: called")
            }
        })

        Log.d(TAG, "loadOwners: ends")
    }


    override fun onCleared() {
        Log.d(TAG, "onCleared: canceling pending downloads")
    }
}