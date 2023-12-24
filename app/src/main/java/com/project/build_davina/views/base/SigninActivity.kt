package com.project.build_davina.views.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.build_davina.databinding.ActivitySigninBinding
import com.project.build_davina.utils.helper.SharedPreferences
import com.project.build_davina.views.admin.layout.DashboardActivity
import com.project.build_davina.views.student.layout.WebviewActivity

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        preferences = SharedPreferences(this)

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this@SigninActivity, SignupActivity::class.java))
        }
        auth = Firebase.auth

        binding.btnSignin.setOnClickListener {
            val email = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            auth.currentUser?.uid?.let { userId ->
                                val userCollection =
                                    Firebase.firestore.collection("user")

                                userCollection.whereEqualTo("id", userId)
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        if (!documents.isEmpty) {
                                            val document = documents.documents[0]
                                            val role = document.get("role").toString()
                                            val id = document.get("id").toString()
                                            if (role == "Student") {
                                                val intent = Intent(this, WebviewActivity::class.java)
                                                preferences.saveString("id", id)
                                                startActivity(intent)
                                                return@addOnSuccessListener
                                            } else if (role == "Admin") {
                                                val intent = Intent(this, DashboardActivity::class.java)
                                                preferences.saveString("id", id)
                                                startActivity(intent)
                                                return@addOnSuccessListener
                                            } else {
                                                Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Failed to get user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }

                        } else {
                            Toast.makeText(this, "Login failed. Double check your email and password.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill in your email and password.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}