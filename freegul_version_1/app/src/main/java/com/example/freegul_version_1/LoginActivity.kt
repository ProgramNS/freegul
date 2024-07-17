package com.example.freegul_version_1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freegul_version_1.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = android.graphics.Color.TRANSPARENT
        }
        // Inisialisasi FirebaseAuth
        auth = FirebaseAuth.getInstance()

        binding.login.setOnClickListener{
            val email = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isNotEmpty() || password.isNotEmpty()) {
                loginUser(email,password)
            }else {
                Toast.makeText(this, "Harap Masukan Email dan Password",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login berhasil, lanjut ke HomeActivity
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Berhasil Login", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    // Jika login gagal, tampilkan pesan kesalahan
                    Toast.makeText(this, "Autentikasi gagal: email atau password salah", Toast.LENGTH_SHORT).show()
                }
            }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null // Bebaskan objek binding saat aktivitas dihancurkan
    }
}



