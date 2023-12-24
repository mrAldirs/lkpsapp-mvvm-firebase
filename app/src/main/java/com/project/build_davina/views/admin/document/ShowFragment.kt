package com.project.build_davina.views.admin.document

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.build_davina.api.viewmodel.DocumentViewModel
import com.project.build_davina.databinding.FragmentAdmDocShowBinding

class ShowFragment : DialogFragment() {
    private lateinit var binding: FragmentAdmDocShowBinding
    private lateinit var parent: DocumentActivity
    private lateinit var documentViewModel: DocumentViewModel
    private lateinit var v: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdmDocShowBinding.inflate(inflater, container, false)
        parent = activity as DocumentActivity
        v = binding.root

        documentViewModel = ViewModelProvider(this).get(DocumentViewModel::class.java)
        val id = arguments?.getString("id").toString()

        documentViewModel.show(id).observe(this, Observer { data ->
            binding.tvName.text = data.name
            binding.tvDesk.text = data.desk
            binding.tvCriteria.text = data.criteria_name
            binding.tvType.text = data.type_name
            binding.tvInfo.text = data.additional_information
            binding.tvCreated.text = data.created_at
            binding.ivFile.setOnClickListener {
                val pdf = data.file.toUri()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(pdf, "application/pdf")
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                v.context.startActivity(intent)
            }
        })

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setTitle("Delete Document")
                .setMessage("Are you sure want to delete this document?")
                .setPositiveButton("Yes") { dialog, which ->
                    documentViewModel.delete(id).observe(this, Observer { result ->
                        if (result) {
                            parent.load()
                            Toast.makeText(v.context, "succeeded in deleting document.", Toast.LENGTH_SHORT).show()
                            dismiss()
                        }
                    })
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

        return v
    }
}