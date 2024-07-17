package com.example.freegul_version_1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.freegul_version_1.databinding.ActivityMonitoringBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.FormBody
import kotlinx.coroutines.withContext
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MonitoringActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonitoringBinding
    private lateinit var auth: FirebaseAuth
    private val authToken = "vYeqh3ro1z9vrKdt1iY7jOEgxkitk9eR"
    private val refreshIntervalMillis = 1000L // Interval pembaruan setiap 1 detik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        // Ambil data dari Intent
        val nik = intent.getStringExtra("nik") ?: ""
        println("NIK: $nik")

        // Tampilkan data pada view
        binding.nik.text = nik

        // Mulai pembaruan otomatis
        startAutoRefresh()

        binding.kirimDataGulaDarah.setOnClickListener {
            val gulaDarah = binding.nilaiGulaDarah.text.toString().toDoubleOrNull()
            if (gulaDarah != null) {
                val token = "7398626423:AAGDgQmRPxCcFIEdZOU2PQeXrntVgBdPmSQ"
                val chatId = "5391296055"
                val message = "Berikut adalah nilai gula darah anda setelah di tes : ${binding.nilaiGulaDarah.text} mg/dl"
                lifecycleScope.launch {
                    sendMessageToTelegram(token, chatId, message)
                }
                Toast.makeText(this, "Data berhasil dikirim", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
                saveDataToDatabase(nik, gulaDarah)
            } else {
                Toast.makeText(this, "Nilai gula darah tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startAutoRefresh() {
        lifecycleScope.launchWhenStarted {
            while (true) {
                try {
                    val result = fetchBlynkData("v1")
                    withContext(Dispatchers.Main) {
                        binding.nilaiGulaDarah.text = result.toString()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MonitoringActivity, "Gagal mengambil data dari Blynk", Toast.LENGTH_SHORT).show()
                    }
                }
                delay(refreshIntervalMillis)
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

    suspend fun sendMessageToTelegram(token:String, chatId:String, message:String){
        val client = OkHttpClient()
        val url = "https://api.telegram.org/bot$token/sendMessage"
        val formBody = FormBody.Builder()
            .add("chat_id", chatId)
            .add("text", message)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        withContext(Dispatchers.IO){
            client.newCall(request).execute().use { response ->
                withContext(Dispatchers.Main){
                if(!response.isSuccessful){
                    Toast.makeText(this@MonitoringActivity, "Gagal mengirim pesan ke Telegram", Toast.LENGTH_SHORT).show()
                } else  {
                    Toast.makeText(this@MonitoringActivity, "Berhasil mengirim pesan ke Telegram", Toast.LENGTH_SHORT).show()
                }
                }
            }
        }
    }

    private fun saveDataToDatabase(nik: String, gulaDarah: Double) {
        val userId = auth.currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance("https://skripsi-a9be0-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val userRef = database.getReference("users").child(userId).child("pasien").child(nik)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val currentDateTime = Calendar.getInstance().time
                    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale.getDefault())
                    dateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
                    val formattedDate = dateFormat.format(currentDateTime)

                    val pasienData = snapshot.getValue(Patient::class.java)
                    pasienData?.let { pasien ->
                        val dataGulaDarah = hashMapOf(
                            "Nilai_Gula_Darah" to gulaDarah,
                            "Tanggal_Pengecekan" to formattedDate
                        )
                        // Simpan data gula darah di dalam node pasien yang sudah ada
                        userRef.child("Data_Gula_Darah").push().setValue(dataGulaDarah)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this@MonitoringActivity, "Data gula darah berhasil disimpan", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@MonitoringActivity, "Gagal menyimpan data gula darah", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this@MonitoringActivity, "NIK tidak ditemukan di database", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MonitoringActivity, "Gagal memeriksa NIK: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}




