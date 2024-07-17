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
                val intent = Intent(this, MonitoringActivity::class.java)
                intent.putExtra("nama_lengkap", binding.namaLengkap.text.toString())
                intent.putExtra("usia", binding.usia.text.toString().toInt())
                intent.putExtra("No_hp", binding.hp.text.toString())
                intent.putExtra("jenis_kelamin", when {
                    binding.laki.isChecked -> "Laki-Laki"
                    binding.perempuan.isChecked -> "Perempuan"
                    else -> ""
                })
                startActivity(intent)
                Toast.makeText(this , "Silahkan Lanjutkan Pengecekan Gula Darah", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show()
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
        val namaLengkap = binding.namaLengkap.text.toString()
        val usia = binding.usia.text.toString()
        val hp = binding.hp.text.toString()
        val jenisKelamin = when {
            binding.laki.isChecked -> "Laki-Laki"
            binding.perempuan.isChecked -> "Perempuan"
            else -> ""
        }

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
        val namaLengkap = binding.namaLengkap.text.toString()
        val usia = binding.usia.text.toString()
        val hp = binding.hp.text.toString()
        val jenisKelamin = when {
            binding.laki.isChecked -> "Laki-Laki"
            binding.perempuan.isChecked -> "Perempuan"
            else -> ""
        }

        return namaLengkap.isNotEmpty() && usia.isNotEmpty() && hp.isNotEmpty() && jenisKelamin.isNotEmpty()
    }

}



