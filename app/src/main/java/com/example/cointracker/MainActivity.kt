package com.example.cointracker

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cointracker.databinding.ActivityMainBinding
import com.example.cointracker.db.CoinDatabase
import com.example.cointracker.model.Asset

/*
Main Activity
almost all generated code
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var coinViewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // create the view model
        val application = this.application
        val dao = CoinDatabase.getInstance(application).coinDao
        val viewModelFactory = CoinViewModelFactory(dao)
        coinViewModel = ViewModelProvider(this, viewModelFactory).get(CoinViewModel::class.java)

        // use fab as refresh button
        binding.appBarMain.fab.setOnClickListener { view ->
            var updated = false
            coinViewModel.trackedListLiveData.value?.forEach {
                // only allow updates every five minutes
                if (it.needsUpdate()) {
                    updated = true
                    val asset = Asset(id = it.assetId, name = it.name, symbol = it.symbol)
                    coinViewModel.updateCoin(asset)
                }
            }
            if (updated){
                Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Data is already current", Toast.LENGTH_SHORT).show()
            }
        }
        binding.appBarMain.fab.hide() // not used, but might use later

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_list, R.id.nav_tracked
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // show fab for tracked list
        navView.setNavigationItemSelectedListener {
            navController.navigate(it.itemId)
            when(it.itemId) {
                R.id.nav_list -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    binding.appBarMain.fab.hide()
                    true
                }
                R.id.nav_tracked -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    binding.appBarMain.fab.show()
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}