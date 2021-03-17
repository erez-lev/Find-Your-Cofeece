package com.cofeece.findyourcofeece

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_timer.*

private const val TAG = "ClientMenuActivity"

class ClientMenuActivity :
    AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {
    /** Activity Attributes: */
    private var mTimerFragment: Fragment? = TimerFragment.newInstance()
    private var mMapFragment: Fragment? = MapFragment.newInstance()
    private var mCurrentFragment = mTimerFragment

    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_menu)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation_client)
        //bottomNav.inflateMenu(R.menu.menu_client)
        bottomNav.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onNavigationItemSelected: called")
        when (item.itemId) {
            R.id.timerMenuItem -> {
                Log.d(TAG, "onNavigationItemSelected: timerMenuItem button pressed")
                if (mCurrentFragment != mTimerFragment) {
                    loadNewFragment(mTimerFragment, mMapFragment)
                    mCurrentFragment = mTimerFragment
                }
                return true
            }

            R.id.mapMenuItem -> {
                Log.d(TAG, "onNavigationItemSelected: mapMenuItem button pressed")
                if (mCurrentFragment != mMapFragment) {
                    loadNewFragment(mMapFragment, mTimerFragment)
                    mCurrentFragment = mMapFragment
                }
                return true
            }

            else -> return false
        }
    }

    /** Class Methods: */
    private fun loadNewFragment(iNewFragment: Fragment?, iOldFragment: Fragment?) {
        Log.d(TAG, "loadNewFragment called")
        if (iNewFragment != null) {
            iOldFragment?.view?.visibility = View.GONE
            timerButton.isEnabled = false
            //iNewFragment.view?.visibility = View.VISIBLE
            switchFragment(iNewFragment)
            Log.d(TAG, "loadNewFragment: is fragment visible? ${iNewFragment.isVisible}")
        } else {
            Log.d(TAG, "loadNewFragment: failed")
            iOldFragment?.view?.visibility = View.VISIBLE
        }
    }

    fun switchFragment(iNextFragment: Fragment?) {
        Log.d(TAG, "switchFragment: called")
        Log.d(TAG, "switchFragment: next fragment is $iNextFragment")

        val fragmentManager = supportFragmentManager

        if (iNextFragment != null) {
            fragmentManager.
            beginTransaction().
            replace(R.id.mainFragment, iNextFragment).
            addToBackStack(null).
            commit()
            iNextFragment.view?.visibility = View.VISIBLE
            if (mCurrentFragment == mTimerFragment) {
                timerButton.visibility = View.GONE
            }
            Log.d(TAG, "getDetailsAndContinueToRate: is fragment visible? ${iNextFragment.isVisible}")
        } else {
            Log.d(TAG, "This fragment is null!")
        }
    }
}