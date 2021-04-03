package com.cofeece.findyourcofeece.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.cofeece.findyourcofeece.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class OwnerMenuActivity : AppCompatActivity(),
      BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_menu)

        BottomNavigationView(this).inflateMenu(R.menu.menu_owner)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home_owner_menu -> {
                return true
            }

            R.id.revenues_owner_menu -> {
                return true
            }

            R.id.deals_owner_menu -> {
                return true
            }

            R.id.settings_owner_menu -> {
                return true
            }

            else -> return false
        }
    }
}
