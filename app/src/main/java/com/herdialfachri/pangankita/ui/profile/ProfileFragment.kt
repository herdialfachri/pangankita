package com.herdialfachri.pangankita.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.herdialfachri.pangankita.R
import com.herdialfachri.pangankita.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileUsername: TextView
    private lateinit var profilePassword: TextView
    private lateinit var titleName: TextView
    private lateinit var titleUsername: TextView
    private lateinit var editProfile: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileName = view.findViewById(R.id.profileName)
        profileEmail = view.findViewById(R.id.profileEmail)
        profileUsername = view.findViewById(R.id.profileUsername)
        profilePassword = view.findViewById(R.id.profilePassword)
        titleName = view.findViewById(R.id.titleName)
        titleUsername = view.findViewById(R.id.titleUsername)
        editProfile = view.findViewById(R.id.editButton)

        // Set toolbar navigation click listener
        val toolbar: Toolbar = view.findViewById(R.id.profileback)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        showAllUserData()

        editProfile.setOnClickListener {
            passUserData()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav?.visibility = View.GONE
    }

    private fun showAllUserData() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
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

                    val intent = Intent(requireContext(), EditProfileActivity::class.java)
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
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }
}
