package com.example.freegul_version_1
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.freegul_version_1.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = android.graphics.Color.TRANSPARENT
            }
        }

        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonMasuk.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)


        }
    }
}