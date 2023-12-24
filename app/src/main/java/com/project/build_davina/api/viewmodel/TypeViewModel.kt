package com.project.build_davina.api.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.build_davina.api.repository.TypeRepository
import com.project.build_davina.models.TypeModel

class TypeViewModel : ViewModel() {
    private val repository = TypeRepository()

    fun getType(callback: (List<TypeModel>?, Exception?) -> Unit) {
        repository.getList(callback)
    }

    fun load() : LiveData<List<TypeModel>> {
        return repository.load()
    }

    fun create(data: TypeModel) : LiveData<Boolean> {
        return repository.create(data)
    }

    fun update(data: TypeModel) : LiveData<Boolean> {
        return repository.update(data)
    }

    fun delete(id: String) : LiveData<Boolean> {
        return repository.delete(id)
    }
}