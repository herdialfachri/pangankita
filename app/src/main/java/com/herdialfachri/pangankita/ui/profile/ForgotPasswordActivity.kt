package com.herdialfachri.pangankita.ui.profile

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.herdialfachri.pangankita.R
import com.herdialfachri.pangankita.ui.login.LoginActivity

class ForgotPasswordActivity : AppCompatActivity() {

    // Declaration
    private lateinit var btnReset: Button
    private lateinit var btnBack: Button
    private lateinit var edtEmail: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var strEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Initialization
        btnBack = findViewById(R.id.btnForgotPasswordBack)
        btnReset = findViewById(R.id.btnReset)
        edtEmail = findViewById(R.id.edtForgotPasswordEmail)
        progressBar = findViewById(R.id.forgetPasswordProgressbar)

        mAuth = FirebaseAuth.getInstance()

        // Reset Button Listener
        btnReset.setOnClickListener {
            strEmail = edtEmail.text.toString().trim()
            if (!TextUtils.isEmpty(strEmail)) {
                resetPassword()
            } else {
                edtEmail.error = "Email field can't be empty"
            }
        }

        // Back Button Code
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun resetPassword() {
        progressBar.visibility = View.VISIBLE
        btnReset.visibility = View.INVISIBLE

        mAuth.sendPasswordResetEmail(strEmail)
            .addOnSuccessListener {
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Reset Password link has been sent to your registered Email",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Error :- " + e.message,
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.INVISIBLE
                btnReset.visibility = View.VISIBLE
            }
    }
}
