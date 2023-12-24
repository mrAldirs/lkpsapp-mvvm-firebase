package com.project.build_davina.views.admin.document

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.build_davina.R
import com.project.build_davina.api.viewmodel.CriteriaViewModel
import com.project.build_davina.api.viewmodel.DocumentViewModel
import com.project.build_davina.api.viewmodel.TypeViewModel
import com.project.build_davina.databinding.FragmentAdmDocFormBinding
import com.project.build_davina.models.DocumentModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class InsertFragment : DialogFragment() {
    private lateinit var binding: FragmentAdmDocFormBinding
    private lateinit var parent: DocumentActivity
    private lateinit var criteriaViewModel: CriteriaViewModel
    private lateinit var typeViewModel: TypeViewModel
    private lateinit var documentViewModel: DocumentViewModel
    private lateinit var v: View
    lateinit var uri: Uri
    val getCriteriaId = mutableListOf<String>()
    var criteriaId = ""
    val getTypeId = mutableListOf<String>()
    var typeId = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdmDocFormBinding.inflate(inflater, container, false)
        parent = activity as DocumentActivity
        v = binding.root

        documentViewModel = ViewModelProvider(this).get(DocumentViewModel::class.java)
        criteriaViewModel = ViewModelProvider(this).get(CriteriaViewModel::class.java)
        typeViewModel = ViewModelProvider(this).get(TypeViewModel::class.java)
        criteria()
        type()

        uri = Uri.EMPTY
        binding.inpPdf.setBackgroundResource(R.drawable.ic_blank)

        binding.btnUpload.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.setType("application/pdf")
            startActivityForResult(intent, 10)
        }

        binding.btnSubmit.setOnClickListener {
            val data = DocumentModel.Document(
                UUID.randomUUID().toString().substring(0, 28).replace("-", ""),
                binding.inpName.text.toString(),
                criteriaId,
                typeId,
                binding.inpDesk.text.toString(),
                binding.inpInfo.text.toString(),
                SimpleDateFormat("dd-MM-yyyy").format(Date()),
            )
            documentViewModel.create(data,uri).observe(this, Observer {
                if (it) {
                    parent.load()
                    Toast.makeText(v.context, "succeeded in creating new document.", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            })
        }

        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == Activity.RESULT_OK) && (requestCode == 10)) {
            if (data != null){
                uri = data.data!!
                binding.inpPdf.setBackgroundResource(R.drawable.ic_pdf)
            }
        }
    }

    private fun criteria() {
        criteriaViewModel.getCriteria { __mKategoris, exception ->
            if (__mKategoris != null) {
                val dataList = mutableListOf<String>()

                dataList.add("-- Select Criteria Document --")

                for (kategori in __mKategoris) {
                    dataList.add(kategori.name)
                    getCriteriaId.add(kategori.id)
                }

                val adapter = ArrayAdapter(v.context, android.R.layout.simple_list_item_1, dataList)
                binding.inpCriteria.adapter = adapter
            }
        }

        binding.inpCriteria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    criteriaId = getCriteriaId[position - 1]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun type() {
        typeViewModel.getType { __mKategoris, exception ->
            if (__mKategoris != null) {
                val dataList = mutableListOf<String>()

                dataList.add("-- Select Type Document --")

                for (kategori in __mKategoris) {
                    dataList.add(kategori.name)
                    getTypeId.add(kategori.id)
                }

                val adapter = ArrayAdapter(v.context, android.R.layout.simple_list_item_1, dataList)
                binding.inpType.adapter = adapter
            }
        }

        binding.inpType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    typeId = getTypeId[position - 1]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}