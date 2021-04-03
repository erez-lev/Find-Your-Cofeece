package com.cofeece.findyourcofeece.firebase

import android.util.Log

import com.cofeece.findyourcofeece.model.user.User
import com.cofeece.findyourcofeece.model.client.Client
import com.cofeece.findyourcofeece.model.owner.Owner

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


private const val TAG = "DatabaseManager"

/** Constants: */
internal const val CLIENTS = "Clients"
internal const val OWNERS = "Owners"

class DatabaseManager() {
    /** Properties: */
    var owners = ArrayList<Owner>()
    var clients = ArrayList<Client>()

    /** Interfaces: */
    interface OnDataCallBack {
        fun onOwnerDataCallBack(owners: ArrayList<Owner>)
//        fun onClientDataCallBack(clients: ArrayList<Client>)
    }

    /** References: */
    val clientRef = FirebaseManager.database.getReference(CLIENTS)
    val ownerRef = FirebaseManager.database.getReference(OWNERS)

    /** Methods: */
    fun writeToDatabase(user: User, type: String) {
        if (!isPropertiesEmpty(user)) {
            when (type) {
                CLIENTS -> {
                    val client = user as Client
                    client.setClientId(clientRef.push().key as String)
                    Log.d(TAG, "$user is being added to the database as $CLIENTS")
                    clientRef.child(client.getClientId()).setValue(client)
                }
                OWNERS -> {
                    val owner = user as Owner
                    Log.d(TAG, "writeToDatabase: ownerRef is ${ownerRef}")
                    owner.setOwnerId(ownerRef.push().key as String)
                    Log.d(TAG, "writeToDatabase: user's id is ${owner.getOwnerId()}")
                    Log.d(TAG, "$user is being added to the database as $OWNERS")
                    ownerRef.child(owner.getOwnerId()).setValue(owner)

                }
                else -> throw Exception("Unknown type was entered")
            }
        } else {
            throw Exception("Register user wasn't succeed!")
        }

    }

    private fun isPropertiesEmpty(user: User): Boolean =
        user.name.isEmpty() || user.username.isEmpty() || user.email.isEmpty() || user.password.isEmpty()


    fun readFromDatabase(type: String, callBack: OnDataCallBack) {
        when (type) {
            OWNERS -> {
                readOwnersData(callBack)
            }

            CLIENTS -> {
                readClientsData(callBack)
            }
        }
    }

    private fun readOwnersData(callBack: OnDataCallBack) {
        ownerRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "readFromDatabase: onDataChange starts")
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    val owner = snapshot.getValue(Owner::class.java)

                    if (owner != null) {
                        owners.add(owner)
                    }
                    Log.d(TAG, "readFromDatabase: onDataChange: owner's name is ${owner?.name}")
                }

                callBack.onOwnerDataCallBack(owners)
                Log.d(TAG, "readFromDatabase: onDataChange ends")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun readClientsData(callBack: OnDataCallBack) {
        clientRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value =
                    dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}