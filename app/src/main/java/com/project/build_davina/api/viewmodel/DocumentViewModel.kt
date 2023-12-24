package com.project.build_davina.api.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.build_davina.api.repository.DocumentRepository
import com.project.build_davina.models.DocumentModel

class DocumentViewModel : ViewModel() {
    private val repository = DocumentRepository()

    fun getChart() : LiveData<List<DocumentModel.ChartDocument>> {
        return repository.getChart()
    }

    fun load(filter: String? = null) : LiveData<List<DocumentModel.Document>> {
        return repository.load(filter)
    }

    fun create(data: DocumentModel.Document, uri: Uri) : LiveData<Boolean> {
        return repository.create(data, uri)
    }

    fun show(id: String) : LiveData<DocumentModel.ShowDocument> {
        return repository.show(id)
    }

    fun delete(id: String) : LiveData<Boolean> {
        return repository.delete(id)
    }
}