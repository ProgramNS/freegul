package com.example.freegul_version_1

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.freegul_version_1.databinding.ActivityInputDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class NewPatientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputDataBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Input Data"
        auth = FirebaseAuth.getInstance()

        // Atur validasi inputan dan tombol kirim
        setupInputValidation()

        binding.kirimData.setOnClickListener {
            if (isValidInput()) {
                saveDataToDatabase()
                val intent = Intent(this, MonitoringActivity::class.java)
                intent.putExtra("nik", binding.nik.text.toString())
                startActivity(intent)
                Toast.makeText(this , "Silahkan Lanjutkan Pengecekan Gula Darah", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Tidak Bisa Lanjut Ada Data Yang Kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupInputValidation() {
        binding.kirimData.isEnabled = false

        // Validasi saat inputan berubah
        binding.namaLengkap.doAfterTextChanged {
            validateInput()
        }

        binding.usia.doAfterTextChanged {
            validateInput()
        }

        binding.hp.doAfterTextChanged {
            validateInput()
        }
        binding.laki.setOnClickListener{
            validateInput()
        }
        binding.perempuan.setOnClickListener {
            validateInput()
        }
    }

    private fun validateInput() {
        val nik = binding.nik.text.toString()
        val namaLengkap = binding.namaLengkap.text.toString()
        val usia = binding.usia.text.toString()
        val hp = binding.hp.text.toString()
        val jenisKelamin = when {
            binding.laki.isChecked -> "Laki-Laki"
            binding.perempuan.isChecked -> "Perempuan"
            else -> ""
        }
        binding.nik.error = if (nik.isEmpty()) "NIK tidak boleh kosong!" else null
        binding.namaLengkap.error = if (namaLengkap.isEmpty()) "Nama lengkap tidak boleh kosong!" else null
        binding.usia.error = if (usia.isEmpty()) "Usia tidak boleh kosong!" else null
        binding.hp.error = if (hp.isEmpty()) "No handphone tidak boleh kosong!" else null
        if (jenisKelamin.isEmpty()) {
            binding.laki.error = "Jenis kelamin tidak boleh kosong!"
            binding.perempuan.error = "Jenis kelamin tidak boleh kosong!"
        } else {
            binding.laki.error = null
            binding.perempuan.error = null
        }

        binding.kirimData.isEnabled = namaLengkap.isNotEmpty() && usia.isNotEmpty() && hp.isNotEmpty() && jenisKelamin.isNotEmpty()
    }

    private fun isValidInput(): Boolean {
        val nik = binding.nik.text.toString()
        val namaLengkap = binding.namaLengkap.text.toString()
        val usia = binding.usia.text.toString()
        val hp = binding.hp.text.toString()
        val jenisKelamin = when {
            binding.laki.isChecked -> "Laki-Laki"
            binding.perempuan.isChecked -> "Perempuan"
            else -> ""
        }

        return nik.isNotEmpty() && namaLengkap.isNotEmpty() && usia.isNotEmpty() && hp.isNotEmpty() && jenisKelamin.isNotEmpty()
    }
    private fun saveDataToDatabase() {
        val nik = binding.nik.text.toString()
        val namaLengkap = binding.namaLengkap.text.toString()
        val usia = binding.usia.text.toString().toInt()
        val hp = binding.hp.text.toString()
        val jenisKelamin = when {
            binding.laki.isChecked -> "Laki-Laki"
            binding.perempuan.isChecked -> "Perempuan"
            else -> ""
        }

        val userId = auth.currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance("https://skripsi-a9be0-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val dataRef = database.getReference("users").child(userId).child("pasien").child(nik)
        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val formattedDate = dateFormat.format(currentDateTime)

        val data = mapOf(
            "NIK" to nik,
            "Nama_Lengkap" to namaLengkap,
            "Usia" to usia,
            "Jenis_Kelamin" to jenisKelamin,
            "No_Handphone" to hp,
            "Tanggal_Pembuatan" to formattedDate
        )

        dataRef.setValue(data).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



