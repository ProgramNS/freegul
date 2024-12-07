package com.example.freegul_version_1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freegul_version_1.databinding.ActivityOldPatientBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OldPatientActivity : AppCompatActivity(), SearchAdapter.OnItemClickListener {
    private lateinit var binding: ActivityOldPatientBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var adapter: SearchAdapter
    private var patientList: MutableList<Patient> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOldPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Pencarian Data Pasien Lama"
        auth = FirebaseAuth.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null){
            val userId = auth.currentUser?.uid ?: return
            database = FirebaseDatabase.getInstance().getReference("users").child(userId).child("pasien")
            adapter = SearchAdapter(patientList, this)
            binding.rvdataPasien.layoutManager = LinearLayoutManager(this)
            binding.rvdataPasien.adapter = adapter

            binding.cari.setOnClickListener {
                val nik = binding.nik.text.toString()
                if (nik.isNotEmpty()) {
                    fetchPatientData(nik)
                } else {

                    Toast.makeText(this, "Masukkan NIK", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun fetchPatientData(nik: String) {
        database.orderByChild("NIK").equalTo(nik).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                patientList.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val patient = dataSnapshot.getValue(Patient::class.java)
                        patient?.let {
                            patientList.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    patientList.clear()
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this@OldPatientActivity, "NIK tidak terdaftar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OldPatientActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(patient: Patient) {
        val intent = Intent(this, MonitoringActivity::class.java)
        intent.putExtra("nik", patient.NIK)
        startActivity(intent)
    }
}
