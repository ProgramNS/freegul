package com.example.freegul_version_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.freegul_version_1.databinding.ActivityInputDataBinding

class InputDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.kirimData.setOnClickListener {
            startActivity(Intent(this, MonitoringActivity::class.java))
        }
    }
}