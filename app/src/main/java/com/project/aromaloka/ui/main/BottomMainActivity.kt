package com.project.aromaloka.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.aromaloka.R
import com.project.aromaloka.databinding.ActivityBottomMainBinding
import com.project.aromaloka.models.ResponseSession
import com.project.aromaloka.ui.main.ui.home.HomeFragment

class BottomMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_main)
        navView.setupWithNavController(navController)

        val session = intent.getParcelableExtra<ResponseSession>("session")
        if (session != null) {
            val homeFragment = navController.currentDestination as? HomeFragment
            homeFragment?.arguments = Bundle().apply {
                putParcelable("session", session)
            }
        }
    }
}
