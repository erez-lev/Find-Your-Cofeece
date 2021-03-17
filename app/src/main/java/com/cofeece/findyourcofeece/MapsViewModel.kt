package com.cofeece.findyourcofeece

import android.os.Looper
import android.util.Log
import androidx.lifecycle.*

import com.cofeece.findyourcofeece.firebase.DatabaseManager

import com.cofeece.findyourcofeece.owner.Owner
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.ValueEventListener

import java.util.*
import java.util.logging.Handler
import kotlin.collections.ArrayList

/** Constants: */
private val TAG = "MapsViewModel"
val EMPTY_OWNER_LIST: MutableList<Owner> = Collections.emptyList()

class MapsViewModel : ViewModel() {
    /** Properties: */
    private val db = DatabaseManager()
    private var owners = EMPTY_OWNER_LIST

    /** Interfaces: */
    interface OnDataCallback {
        fun onDataCallBack(values: ArrayList<Owner>)
    }

    /** Methods: */
    init {
        Log.d(TAG, "init: called")
    }

    fun getOwnerList(): MutableList<Owner> = owners

    fun setOwnerList(owners: ArrayList<Owner>) {
        Log.d(TAG, "setOwnerList: called")

        Log.d(TAG, "loadOwners: onDataChanged: owners value is ${this.owners}")
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