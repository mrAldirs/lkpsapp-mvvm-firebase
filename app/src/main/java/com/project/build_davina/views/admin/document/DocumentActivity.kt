package com.project.build_davina.views.admin.document

import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.build_davina.adapter.DocumentAdapter
import com.project.build_davina.api.viewmodel.CriteriaViewModel
import com.project.build_davina.api.viewmodel.DocumentViewModel
import com.project.build_davina.databinding.ActivityAdmDocumentBinding

class DocumentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdmDocumentBinding
    private lateinit var documentViewModel: DocumentViewModel
    private lateinit var criteriaViewModel: CriteriaViewModel
    private lateinit var adapter: DocumentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdmDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Document"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        documentViewModel = ViewModelProvider(this).get(DocumentViewModel::class.java)
        criteriaViewModel = ViewModelProvider(this).get(CriteriaViewModel::class.java)
        adapter = DocumentAdapter(listOf(), this)
        binding.rvDocument.layoutManager = LinearLayoutManager(this)
        binding.rvDocument.adapter = adapter

        binding.btnCreate.setOnClickListener {
            val dialog = InsertFragment()

            dialog.show(supportFragmentManager, "InsertFragment")
        }

        load()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun load() {
        val getCriteriaId = mutableListOf<String>()
        val dataList = mutableListOf<String>()
        val adapterCriteria: ArrayAdapter<String> by lazy {
            ArrayAdapter(this, R.layout.simple_list_item_1, dataList)
        }

        criteriaViewModel.getCriteria { __mKategoris, exception ->
            if (__mKategoris != null) {
                dataList.add("-- All Criteria --")

                for (kategori in __mKategoris) {
                    dataList.add(kategori.name)
                    getCriteriaId.add(kategori.id)
                }

                binding.inpFilter.adapter = adapterCriteria
            }
        }

        binding.inpFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()

                if (selectedItem == "-- All Criteria --") {
                    documentViewModel.load().observe(this@DocumentActivity, Observer {
                        adapter.setData(it)
                    })
                } else {
                    if (position > 0 && position <= getCriteriaId.size) {
                        val selectedItemId = getCriteriaId[position - 1]

                        documentViewModel.load(selectedItemId).observe(this@DocumentActivity, Observer {
                            adapter.setData(it)
                        })
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}