package com.example.freegul_version_1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.freegul_version_1.databinding.ActivityMonitoringBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MonitoringActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonitoringBinding
    private lateinit var auth: FirebaseAuth
    private val authToken = "vYeqh3ro1z9vrKdt1iY7jOEgxkitk9eR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        // Ambil data dari Intent
        val namaLengkap = intent.getStringExtra("nama_lengkap") ?: ""
        val usia = intent.getIntExtra("usia", 0)
        val hp = intent.getStringExtra("No_hp") ?: ""
        val jenisKelamin = intent.getStringExtra("jenis_kelamin") ?: ""

        // Tampilkan data pada view
        binding.namaLengkap.text = namaLengkap
        binding.usia.text = usia.toString()
        binding.hp.text = hp
        binding.jenisKelamin.text = jenisKelamin

        // Ambil data dari Blynk
        lifecycleScope.launch {
            try {
                val result = fetchBlynkData("v1")
                binding.nilaiGulaDarah.text = result.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MonitoringActivity, "Gagal mengambil data dari Blynk", Toast.LENGTH_SHORT).show()
            }
        }

        binding.kirimDataGulaDarah.setOnClickListener {
            if (isValidInput()) {
                saveDataToDatabase(
                    binding.namaLengkap.text.toString(),
                    binding.usia.text.toString().toInt(),
                    binding.hp.text.toString(),
                    binding.jenisKelamin.text.toString(),
                    binding.nilaiGulaDarah.text.toString().toDouble()
                )
                Toast.makeText(this, "Data berhasil dikirim", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Tidak bisa kirim data karena ada data yang kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun fetchBlynkData(pin: String): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val url = "https://sgp1.blynk.cloud/external/api/get?token=$authToken&$pin"
                val response = URL(url).readText() // Mengambil respons dari URL
                response.toDoubleOrNull() ?: response // Coba konversi ke double, jika gagal kembalikan sebagai string
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun saveDataToDatabase(namaLengkap: String, usia: Int, hp: String, jenisKelamin: String, gulaDarah: Double) {
        val userId = auth.currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance("https://skripsi-a9be0-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val dataRef = database.getReference("users").child(userId).child("pasien").push()
        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
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
        return binding.namaLengkap.text.isNotEmpty() && binding.usia.text.isNotEmpty() && binding.hp.text.isNotEmpty() && binding.jenisKelamin.text.isNotEmpty() && binding.nilaiGulaDarah.text.isNotEmpty()
    }
}



