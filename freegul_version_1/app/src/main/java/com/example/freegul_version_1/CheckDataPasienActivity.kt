package com.example.freegul_version_1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.freegul_version_1.databinding.ActivityCheckDataPasienBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class CheckDataPasienActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityCheckDataPasienBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Check Data Pasien"
        auth = FirebaseAuth.getInstance()
        binding = ActivityCheckDataPasienBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.baru.setOnClickListener {
            val intent = Intent(this, NewPatientActivity::class.java)
            startActivity(intent)
        }
        binding.lama.setOnClickListener {
            val intent = Intent(this, OldPatientActivity::class.java)
            startActivity(intent)
        }
        setupBottomNavigationView()
    }
    private fun setupBottomNavigationView() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_home -> {
                // Handle navigation home action if needed
                true
            }
            R.id.navigation_tengah -> {
                // Handle navigation to middle action if needed
                true
            }
            R.id.navigation_logout -> {
                logoutUser()
                true
            }
            else -> false
        }
    }

    private fun logoutUser() {
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Bebaskan objek binding saat aktivitas dihancurkan
    }
}