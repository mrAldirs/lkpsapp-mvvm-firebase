package com.project.build_davina.views.base

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.build_davina.api.viewmodel.UserViewModel
import com.project.build_davina.databinding.ActivitySignupBinding
import com.project.build_davina.models.StudentModel
import com.project.build_davina.models.UserModel

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Sign Up"
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val db = Firebase.firestore
        val optionsCollection = db.collection("role")

        optionsCollection.get()
            .addOnSuccessListener { documents ->
                val optionsList = mutableListOf<String>()

                for (document in documents) {
                    val optionValue = document.getString("name")
                    optionValue?.let {
                        optionsList.add(it)
                    }
                }

                val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, optionsList)
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                binding.inpRole.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal mengambil data dari Firestore: ${e.message}", Toast.LENGTH_LONG).show()
            }

        binding.btnSubmit.setOnClickListener {
            val email = binding.inpEmail.text.toString()
            val password = binding.inpPassword.text.toString()
            val username = binding.inpUsername.text.toString()
            val selectedOption: String = binding.inpRole.selectedItem.toString()

            userViewModel.registerUser(email, password) { success, id ->
                if (success) {
                    val dataList = id?.let { it1 ->
                        UserModel(
                            it1,username, email,password, selectedOption
                        )
                    }

                    dataList?.let { it1 ->
                        userViewModel.createUser(it1).observe(this, Observer {
                            Toast.makeText(this, "Success in create new account", Toast.LENGTH_SHORT).show()
                        })
                    }

                    if (selectedOption.equals("Student")) {
                        val data = StudentModel.Student(
                            id.toString(),
                            binding.inpName.text.toString(),
                            binding.inpNim.text.toString(),
                        )
                        userViewModel.createSingle(data, "student")
                            .observe(this, Observer {
                                onBackPressed()
                                Toast.makeText(this, "Please validate your email!", Toast.LENGTH_SHORT).show()
                            })
                    } else {
                        onBackPressed()
                        Toast.makeText(this, "Please validate your email!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Failed to create account.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}