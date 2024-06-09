package com.example.freegul_version_1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freegul_version_1.databinding.ActivityMonitoringBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
class MonitoringActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonitoringBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        // Ambil data dari Intent
        val namaLengkap = intent.getStringExtra("nama_lengkap")
        val usia = intent.getIntExtra("usia", 0)
        val hp = intent.getStringExtra("No_hp")
        val jenisKelamin = intent.getStringExtra("jenis_kelamin")

        // Tampilkan data pada view
        binding.namaLengkap.text = namaLengkap
        binding.usia.text = usia.toString()
        binding.hp.text = hp
        binding.jenisKelamin.text = jenisKelamin

        binding.kirimDataGulaDarah.setOnClickListener {
            if (isValidInput()) {
                saveDataToDatabase(
                    binding.namaLengkap.text.toString(),
                    binding.usia.text.toString().toInt(),
                    binding.hp.text.toString(),
                    binding.jenisKelamin.text.toString(),
                    binding.nilaiGulaDarah.text.toString().toInt()
                )
                Toast.makeText(this, "Data berhasil dikirim", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Tidak bisa kirim data karena ada data yang kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveDataToDatabase(namaLengkap: String, usia: Int, hp: String, jenisKelamin: String, gulaDarah: Int) {
        val userId = auth.currentUser?.uid ?: return // Dapatkan UID pengguna yang sedang login
        val database = FirebaseDatabase.getInstance("https://skripsi-a9be0-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val dataRef = database.getReference("users").child(userId).child("pasien").push() // Path ke lokasi Realtime Database Anda
        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta") // WIB
        val formattedDate = dateFormat.format(currentDateTime)
        val data = linkedMapOf(
            "nama_lengkap" to namaLengkap,
            "usia" to usia,
            "jenis_kelamin" to jenisKelamin,
            "no_handphone" to hp,
            "nilai_gula_Darah" to gulaDarah,
            "tanggal" to formattedDate

        )
        dataRef.setValue(data)
    }

    private fun isValidInput(): Boolean {
        val namaLengkap = binding.namaLengkap.text.toString()
        val usia = binding.usia.text.toString()
        val hp = binding.hp.text.toString()
        val jenisKelamin = binding.jenisKelamin.text.toString()

        return namaLengkap.isNotEmpty() && usia.isNotEmpty() && hp.isNotEmpty() && jenisKelamin.isNotEmpty()
    }
}
