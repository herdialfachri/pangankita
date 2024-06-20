package com.herdialfachri.pangankita.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.herdialfachri.pangankita.MainActivity
import com.herdialfachri.pangankita.R

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splashscreen)

        // Transition to the MainActivity after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close the SplashScreenActivity
        }, 3000) // 3000 milliseconds delay
    }

    private fun enableEdgeToEdge() {
        // Implement edge-to-edge functionality if needed
    }
}
