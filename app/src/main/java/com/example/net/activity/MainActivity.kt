package com.example.net.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.net.R
import com.example.net.databinding.ActivityMainBinding
import com.example.net.fragments.FirstScreenFragment

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // Set up the ActionBar with the Navigation UI
        setupActionBarWithNavController(navController ?: return)
        keyOfTheEncryption = stringFromJNI()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController?.navigateUp() ?: return super.onSupportNavigateUp()
    }


    override fun onBackPressed() {
        navController?.popBackStack()
    }

    external fun stringFromJNI(): String

    companion object {
        init {
            System.loadLibrary("net")
        }
        var keyOfTheEncryption: String = ""
    }
}