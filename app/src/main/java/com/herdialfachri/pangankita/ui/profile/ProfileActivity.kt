package com.herdialfachri.pangankita.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.herdialfachri.pangankita.R
import com.herdialfachri.pangankita.ui.login.LoginActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileUsername: TextView
    private lateinit var profilePassword: TextView
    private lateinit var titleName: TextView
    private lateinit var titleUsername: TextView
    private lateinit var editProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is already logged in
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_profile)

        profileName = findViewById(R.id.profileName)
        profileEmail = findViewById(R.id.profileEmail)
        profileUsername = findViewById(R.id.profileUsername)
        profilePassword = findViewById(R.id.profilePassword)
        titleName = findViewById(R.id.titleName)
        titleUsername = findViewById(R.id.titleUsername)
        editProfile = findViewById(R.id.editButton)

        showAllUserData()

        editProfile.setOnClickListener {
            passUserData()
        }
    }

    private fun showAllUserData() {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val nameUser = sharedPreferences.getString("name", "")
        val emailUser = sharedPreferences.getString("email", "")
        val usernameUser = sharedPreferences.getString("username", "")
        val passwordUser = sharedPreferences.getString("password", "")

        titleName.text = nameUser
        titleUsername.text = usernameUser
        profileName.text = nameUser
        profileEmail.text = emailUser
        profileUsername.text = usernameUser
        profilePassword.text = passwordUser
    }

    private fun passUserData() {
        val userUsername = profileUsername.text.toString().trim()

        val reference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserDatabase = reference.orderByChild("username").equalTo(userUsername)

        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val nameFromDB =
                        snapshot.child(userUsername).child("name").getValue(String::class.java)
                    val emailFromDB =
                        snapshot.child(userUsername).child("email").getValue(String::class.java)
                    val usernameFromDB =
                        snapshot.child(userUsername).child("username").getValue(String::class.java)
                    val passwordFromDB =
                        snapshot.child(userUsername).child("password").getValue(String::class.java)

                    val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
                    intent.putExtra("name", nameFromDB)
                    intent.putExtra("email", emailFromDB)
                    intent.putExtra("username", usernameFromDB)
                    intent.putExtra("password", passwordFromDB)

                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}