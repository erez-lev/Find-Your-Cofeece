package com.cofeece.findyourcofeece

import android.os.Looper
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import com.cofeece.findyourcofeece.firebase.DatabaseManager
import com.cofeece.findyourcofeece.firebase.OWNERS
import com.cofeece.findyourcofeece.owner.Owner
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

interface OnDataReady {
    fun onDataReady()
}

/** Constants: */
private val TAG = "MapsViewModel"

class MapsViewModel() : ViewModel() {
    /** Properties: */
    private val db = DatabaseManager()
    private var owners = MutableLiveData<ArrayList<Owner>>()
    val ownersData: LiveData<ArrayList<Owner>>
        get() = owners
    private var singleOwner = MutableLiveData<Owner>()
    val singleOwnersData: LiveData<Owner>
        get() = singleOwner
    var mapFragmentListener: OnDataReady? = null
    private val mUiHandler = android.os.Handler(Looper.getMainLooper())

    /** Methods: */
    init {
        Log.d(TAG, "init: called")
        loadOwners()
    }

    fun loadOwnersTest() {
        Log.d(TAG, "loadOwnersTest: starts")
        db.readFromDatabase(OWNERS, object : DatabaseManager.OnDataCallBack {
            override fun onOwnerDataCallBack(owners: ArrayList<Owner>) {
                this@MapsViewModel.owners.postValue(owners)
            }
        })
    }

    private fun loadOwners() {
        Log.d(TAG, "loadOwners: starts")
        getDataFromDbAndUpdate()
        Log.d(TAG, "loadOwners: ends")
    }

    private fun getDataFromDbAndUpdate() {
        Log.d(TAG, "getDataFromDbAndUpdate: starts")
        val ownerList = ArrayList<Owner>()

//        Log.d(TAG, "getDataFromDbAndUpdate: starts (inside thread)")
        db.ownerRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: starts")
                fetchDataAndAddToOwnerList(dataSnapshot, ownerList)
                postUpdateToMainThread(ownerList)
                Log.d(TAG, "onDataChange: ends")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "onCancelled: called")
            }
        })
//        Log.d(TAG, "getDataFromDbAndUpdate: ends (inside thread)")
        Log.d(TAG, "getDataFromDbAndUpdate: ends")
    }

    private fun fetchDataAndAddToOwnerList(dataSnapshot: DataSnapshot, ownerList: ArrayList<Owner>) {
        dataSnapshot.children.forEach { data ->
            val owner = data.getValue(Owner::class.java)
            if (owner != null) {
                ownerList.add(owner)
            } else {
                throw Exception("There was error with reading data from Firebase!")
            }
        }
    }

    private fun postUpdateToMainThread(ownerList: ArrayList<Owner>) {
        owners.postValue(ownerList)
        mapFragmentListener!!.onDataReady()
    }

    fun clearOwnerList() {
        owners.value = ArrayList()
        Log.d(TAG, "clearOwnerList: owners live data value is ${owners.value}")
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared: canceling pending downloads")
    }
}