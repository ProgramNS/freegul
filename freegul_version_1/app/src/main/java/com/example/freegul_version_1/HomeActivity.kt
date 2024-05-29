package com.example.freegul_version_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.freegul_version_1.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gulaDarah.setOnClickListener{
            val intent = Intent(this,InputDataActivity::class.java)
            startActivity(intent)
        }

        binding.historyData.setOnClickListener{
            startActivity(Intent(this,HistoryActivity::class.java))
        }
    }
}