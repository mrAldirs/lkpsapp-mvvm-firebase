package com.project.build_davina.views.student.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.build_davina.api.viewmodel.StudentViewModel
import com.project.build_davina.databinding.ActivityMhsProfileBinding
import com.project.build_davina.models.StudentModel
import com.project.build_davina.utils.helper.SharedPreferences

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMhsProfileBinding
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMhsProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferences = SharedPreferences(this)
        studentViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)

        show(preferences.getString("id", ""))

        binding.btnSubmit.setOnClickListener {
            val name = binding.inpName.text.toString()
            val nim = binding.inpNim.text.toString()
            val email = binding.inpEmail.text.toString()
            val username = binding.inpUsername.text.toString()

            val data = StudentModel.ShowStudent(
                preferences.getString("id", ""),
                name,
                nim,
                email,
                username
            )

            studentViewModel.update(data).observe(this, Observer { status ->
                if (status) {
                    Toast.makeText(this, "Success in update your profile", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun show(id: String) {
        studentViewModel.show(id).observe(this, Observer { data ->
            binding.inpName.setText(data.name)
            binding.inpNim.setText(data.nim)
            binding.inpEmail.setText(data.email)
            binding.inpUsername.setText(data.username)
        })
    }
}