package com.cofeece.findyourcofeece

import android.os.Looper
import android.util.Log
import androidx.lifecycle.*

import com.cofeece.findyourcofeece.firebase.DatabaseManager
import com.cofeece.findyourcofeece.firebase.OWNERS

import com.cofeece.findyourcofeece.owner.Owner
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.ValueEventListener

import java.util.*
import java.util.logging.Handler
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

/** Constants: */
private val TAG = "MapsViewModel"
val EMPTY_OWNER_LIST: MutableList<Owner> = Collections.emptyList()

class MapsViewModel : ViewModel() {
    /** Properties: */
    private val db = DatabaseManager()
//    private var owners = EMPTY_OWNER_LIST
    private var owners = MutableLiveData<ArrayList<Owner>>()
    var ownersData: LiveData<ArrayList<Owner>>
        get() = owners
        set(value) {ownersData = value}

    /** Methods: */
    init {
        Log.d(TAG, "init: called")
    }

    fun loadOwnersTest() {
        db.readFromDatabase(OWNERS, object: DatabaseManager.OnDataCallBack {
            override fun onOwnerDataCallBack(owners: ArrayList<Owner>) {
                this@MapsViewModel.owners.postValue(owners)
            }
        })
    }

    fun loadOwners(): LiveData<ArrayList<Owner>> {
        Log.d(TAG, "loadOwners: starts")
        val ownerList = ArrayList<Owner>()

        db.ownerRef.addListenerForSingleValueEvent(object: ValueEventListener {
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

                owners.postValue(ownerList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "onCancelled: called")
            }
        })


        Log.d(TAG, "loadOwners: ends")
        return owners
    }

    fun clearOwnerList() {
        owners.value = ArrayList()
        Log.d(TAG, "clearOwnerList: owners live data value is ${owners.value}")
    }


    override fun onCleared() {
        Log.d(TAG, "onCleared: canceling pending downloads")
    }
}