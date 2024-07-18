package com.example.freegul_version_1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freegul_version_1.databinding.ActivityHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView
import com.example.freegul_version_1.databinding.DialogDataGulaDarahBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryActivity : AppCompatActivity(), PatientAdapter.OnItemClickListener {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var database: DatabaseReference
    private lateinit var patientAdapter: PatientAdapter
    private val patients = mutableListOf<Patient>()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        supportActionBar?.title = "History Data"

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = auth.currentUser?.uid ?: return

            database = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("pasien")

            binding.rvdataPasien.layoutManager = LinearLayoutManager(this)
            patientAdapter = PatientAdapter(patients, this)
            binding.rvdataPasien.adapter = patientAdapter

            fetchPatientsFromFirebase()
        } else {
            Log.e("HistoryActivity", "No user is signed in.")
        }
        setupBottomNavigationView()
    }

    private fun setupBottomNavigationView() {
        binding.navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    backToHome()
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
    }

    private fun logoutUser() {
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun backToHome(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun fetchPatientsFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                patients.clear()
                for (patientSnapshot in snapshot.children) {
                    val patient = patientSnapshot.getValue(Patient::class.java)
                    if (patient != null) {
                        patients.add(patient)
                    }
                }
                patientAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HistoryActivity", "Failed to read patients", error.toException())
            }
        })
    }

    override fun onItemClick(position: Int) {
        val selectedPatient = patients[position]
        val dataGulaDarahSnapshot = selectedPatient.Data_Gula_Darah

        val dataGulaDarah = dataGulaDarahSnapshot.map { entry ->
            Glucose(entry.value.Nilai_Gula_Darah, entry.value.Tanggal_Pengecekan)
        }

        showDataGulaDarahDialog(dataGulaDarah)
    }

    @SuppressLint("SetTextI18n")
    private fun showDataGulaDarahDialog(dataGulaDarah: List<Glucose>) {
        val dialogBinding = DialogDataGulaDarahBinding.inflate(LayoutInflater.from(this))
        val linearLayout = dialogBinding.linearLayoutDataGulaDarah
        linearLayout.removeAllViews() // Remove any existing views

        if (dataGulaDarah.isNotEmpty()) {
            for (glucose in dataGulaDarah) {
                val textView = TextView(this).apply {
                    text = "Gula Darah: ${glucose.Nilai_Gula_Darah}, Tanggal Pengecekan: ${glucose.Tanggal_Pengecekan}"
                    textSize = 16f
                    setPadding(4, 2, 4, 2)
                }
                linearLayout.addView(textView)
            }
        } else {
            val textView = TextView(this).apply {
                text = "No Data"
                textSize = 16f
                setPadding(4, 2, 4, 2)
            }
            linearLayout.addView(textView)
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setPositiveButton("OK", null)
            .show()
    }
}
