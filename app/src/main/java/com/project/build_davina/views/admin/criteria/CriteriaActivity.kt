package com.project.build_davina.views.admin.criteria

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.build_davina.adapter.CriteriaAdapter
import com.project.build_davina.api.viewmodel.CriteriaViewModel
import com.project.build_davina.databinding.ActivityAdmCriteriaBinding
import com.project.build_davina.models.CriteriaModel
import java.util.UUID

class CriteriaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdmCriteriaBinding
    private lateinit var criteriaViewModel: CriteriaViewModel
    private lateinit var adapter: CriteriaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdmCriteriaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Criteria Document"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        criteriaViewModel = ViewModelProvider(this).get(CriteriaViewModel::class.java)
        adapter = CriteriaAdapter(listOf(), this)
        binding.rvDocument.layoutManager = LinearLayoutManager(this)
        binding.rvDocument.adapter = adapter

        load()

        binding.btnInsert.setOnClickListener { 
            create()
        }
    }

    private fun load() {
        criteriaViewModel.load().observe(this, Observer { data ->
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
        val data = CriteriaModel(
            UUID.randomUUID().toString().substring(0, 28).replace("-", ""),
            binding.inpDesk.text.toString()
        )
        criteriaViewModel.create(data).observe(this, Observer { status ->
            if (status) {
                load()
                binding.inpDesk.text?.clear()
                binding.inpDesk.clearFocus()
                Toast.makeText(this, "succeeded in creating new criteria.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun update(id: String) {
        val data = CriteriaModel(
            id,
            binding.inpDesk.text.toString()
        )
        criteriaViewModel.update(data).observe(this, Observer { status ->
            if (status) {
                load()
                binding.inpDesk.text?.clear()
                binding.inpDesk.clearFocus()
                Toast.makeText(this, "succeeded in updating criteria.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun delete(id: String) {
        criteriaViewModel.delete(id).observe(this, Observer { status ->
            if (status) {
                load()
                binding.inpDesk.text?.clear()
                binding.inpDesk.clearFocus()
                Toast.makeText(this, "succeeded in deleting criteria.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}