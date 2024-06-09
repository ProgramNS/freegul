package com.example.freegul_version_1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freegul_version_1.databinding.ActivityHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryActivity : AppCompatActivity() {
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
        // Get the current user from Firebase Authentication
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = auth.currentUser?.uid ?: return

            // Initialize Firebase Database reference
            database = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("pasien")

            binding.rvdataPasien.layoutManager = LinearLayoutManager(this)
            patientAdapter = PatientAdapter(patients)
            binding.rvdataPasien.adapter = patientAdapter

            fetchPatientsFromFirebase()
        } else {
            Log.e("HistoryActivity", "No user is signed in.")
            // Handle the case where no user is signed in, e.g., redirect to login screen
        }
    }

    private fun fetchPatientsFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
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
}
