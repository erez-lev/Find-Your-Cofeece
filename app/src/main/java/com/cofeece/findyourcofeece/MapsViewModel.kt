package com.cofeece.findyourcofeece

import android.util.Log
import androidx.lifecycle.*

import com.cofeece.findyourcofeece.firebase.DatabaseManager

import com.cofeece.findyourcofeece.owner.Owner
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import java.lang.Exception

import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


private const val TAG = "MapsViewModel"
val EMPTY_OWNER_LIST: List<Owner> = Collections.emptyList()

class MapsViewModel : ViewModel() {

    private val db = DatabaseManager()

    private val owners = MutableLiveData<List<Owner>>()
    val ownerList: LiveData<List<Owner>>
        get() = owners

    interface OnDataCallback {
        fun onDataCallBack(values: ArrayList<Owner>)
    }


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



//    fun loadOwners() {
//        Log.d(TAG, "loadOwners: starts")
//        Log.d(TAG, "loadOwners: before initializing,  owner's value is ${this.ownerList.value} or ${this.owners.value}")
//
//        val owners = ArrayList<Owner>()
//        viewModelScope.launch {
//            get(owners)
//        }
//
//        this.owners.postValue(owners)
//
//        Log.d(TAG, "loadOwners: after updating,  owner's value is ${this.ownerList.value?.get(0)} or ${this.owners.value?.get(0)}")
//        Log.d(TAG, "loadOwners: ends")
//    }
//
//    suspend fun get(owners: ArrayList<Owner>) = withContext(Dispatchers.IO) {
//
//        db.ownerRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                Log.d(TAG, "onDataChange: called")
//                for (snapshot in dataSnapshot.children) {
//                    val owner = snapshot.getValue(Owner::class.java)
//
//                    if (owner != null) {
//                        owners.add(owner)
//                    } else {
//                        Log.d(TAG, "loadOwners: onDataChanged: owner is $owner")
//                    }
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.d(TAG, "onCancelled: called")
//
//            }
//        })
//    }


    override fun onCleared() {
        Log.d(TAG, "onCleared: canceling pending downloads")
    }
}