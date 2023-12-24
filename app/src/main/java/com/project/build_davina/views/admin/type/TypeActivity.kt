package com.project.build_davina.views.admin.type

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.build_davina.adapter.TypeAdapter
import com.project.build_davina.api.viewmodel.TypeViewModel
import com.project.build_davina.databinding.ActivityAdmTypeBinding
import com.project.build_davina.models.TypeModel
import java.util.UUID

class TypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdmTypeBinding
    private lateinit var typeViewModel: TypeViewModel
    private lateinit var adapter: TypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdmTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Type Document"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        typeViewModel = ViewModelProvider(this).get(TypeViewModel::class.java)

        adapter = TypeAdapter(listOf(), this)
        binding.rvType.layoutManager = LinearLayoutManager(this)
        binding.rvType.adapter = adapter

        binding.btnInsert.setOnClickListener {
            create()
        }

        load()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun load() {
        typeViewModel.load().observe(this, Observer { data ->
            adapter.setData(data)
        })
    }

    fun selector(id: String, name: String) {
        binding.inpDesk.setText(name)
        binding.btnEdit.setOnClickListener {
            update(id)
        }
        binding.btnDelete.setOnClickListener {
            delete(id)
        }
    }

    private fun create() {
        if (binding.inpDesk.text.toString().isEmpty()) {
            binding.inpDesk.error = "Name is required!"
            binding.inpDesk.requestFocus()
            return
        }

        val data = TypeModel(
            UUID.randomUUID().toString().substring(0, 28).replace("-", ""),
            binding.inpDesk.text.toString()
        )

        typeViewModel.create(data).observe(this, Observer { status ->
            if (status) {
                binding.inpDesk.text?.clear()
                binding.inpDesk.clearFocus()
                load()
                Toast.makeText(this, "succeeded in creating new type.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun update(id: String) {
        if (binding.inpDesk.text.toString().isEmpty()) {
            binding.inpDesk.error = "Name is required!"
            binding.inpDesk.requestFocus()
            return
        }

        val data = TypeModel(
            id,
            binding.inpDesk.text.toString()
        )

        typeViewModel.update(data).observe(this, Observer { status ->
            if (status) {
                binding.inpDesk.text?.clear()
                binding.inpDesk.clearFocus()
                load()
                Toast.makeText(this, "succeeded in updating type.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun delete(id: String) {
        typeViewModel.delete(id).observe(this, Observer { status ->
            if (status) {
                binding.inpDesk.text?.clear()
                binding.inpDesk.clearFocus()
                load()
                Toast.makeText(this, "succeeded in deleting type.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}