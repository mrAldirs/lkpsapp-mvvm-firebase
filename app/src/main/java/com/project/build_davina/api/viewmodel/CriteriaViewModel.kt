package com.project.build_davina.api.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.build_davina.api.repository.CriteriaRepository
import com.project.build_davina.models.CriteriaModel

class CriteriaViewModel : ViewModel() {
    private val repository = CriteriaRepository()

    fun getCriteria(callback: (List<CriteriaModel>?, Exception?) -> Unit) {
        repository.getList(callback)
    }

    fun load() : LiveData<List<CriteriaModel>> {
        return repository.load()
    }

    fun create(data: CriteriaModel) : LiveData<Boolean> {
        return repository.create(data)
    }

    fun update(data: CriteriaModel) : LiveData<Boolean> {
        return repository.update(data)
    }

    fun delete(id: String) : LiveData<Boolean> {
        return repository.delete(id)
    }
}