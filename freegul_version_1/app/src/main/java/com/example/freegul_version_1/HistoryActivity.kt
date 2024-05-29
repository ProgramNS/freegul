package com.example.freegul_version_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freegul_version_1.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val patients = listOf(
            Patient("Selasa, 26/03/24 19:59", "John Doe", 30, 120, "089531580002"),
            Patient("Selasa, 26/03/24 19:20", "Jane Doe", 25, 110, "089531580003"),
            Patient("Selasa, 26/03/24 18:39", "Jim Beam", 45, 140, "089531580004"),
            Patient("Selasa, 26/03/24 15:21", "Alice Smith", 50, 115, "089531580005"),
            Patient("Selasa, 26/03/24 12:02", "Bob Johnson", 60, 130, "089531580006")
        )

        binding.rvdataPasien.layoutManager = LinearLayoutManager(this)
        binding.rvdataPasien.adapter = PatientAdapter(patients)
    }
}