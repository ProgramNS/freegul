package com.example.freegul_version_1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.freegul_version_1.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener{
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null // Bebaskan objek binding saat aktivitas dihancurkan
    }
}
