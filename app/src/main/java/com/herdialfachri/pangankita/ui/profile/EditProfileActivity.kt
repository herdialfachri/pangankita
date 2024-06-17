package com.herdialfachri.pangankita.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.herdialfachri.pangankita.MainActivity
import com.herdialfachri.pangankita.R

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var saveButton: Button
    private lateinit var reference: DatabaseReference

    private lateinit var nameUser: String
    private lateinit var emailUser: String
    private lateinit var usernameUser: String
    private lateinit var passwordUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        reference = FirebaseDatabase.getInstance().getReference("users")

        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)
        saveButton = findViewById(R.id.saveButton)

        showData()

        saveButton.setOnClickListener {
            if (isNameChanged() || isPasswordChanged() || isEmailChanged()) {
                Toast.makeText(this, "Profile change successful", Toast.LENGTH_SHORT).show()
                // Navigate back to MainActivity after saving data
                navigateToMainActivity()
            } else {
                Toast.makeText(this, "Username can't be change", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isNameChanged(): Boolean {
        return if (nameUser != editName.text.toString()) {
            reference.child(usernameUser).child("name").setValue(editName.text.toString())
            nameUser = editName.text.toString()
            true
        } else {
            false
        }
    }

    private fun isEmailChanged(): Boolean {
        return if (emailUser != editEmail.text.toString()) {
            reference.child(usernameUser).child("email").setValue(editEmail.text.toString())
            emailUser = editEmail.text.toString()
            true
        } else {
            false
        }
    }

    private fun isPasswordChanged(): Boolean {
        return if (passwordUser != editPassword.text.toString()) {
            reference.child(usernameUser).child("password").setValue(editPassword.text.toString())
            passwordUser = editPassword.text.toString()
            true
        } else {
            false
        }
    }

    private fun showData() {
        val intent = intent
        nameUser = intent.getStringExtra("name").toString()
        emailUser = intent.getStringExtra("email").toString()
        usernameUser = intent.getStringExtra("username").toString()
        passwordUser = intent.getStringExtra("password").toString()

        editName.setText(nameUser)
        editEmail.setText(emailUser)
        editUsername.setText(usernameUser)
        editPassword.setText(passwordUser)
    }

    private fun navigateToMainActivity() {
        // Save user data to shared preferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", nameUser)
        editor.putString("email", emailUser)
        editor.putString("username", usernameUser)
        editor.putString("password", passwordUser)
        editor.apply()

        // Navigate to MainActivity with an extra flag
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("navigateToNotifications", true)
        startActivity(intent)
        finish()
    }
}