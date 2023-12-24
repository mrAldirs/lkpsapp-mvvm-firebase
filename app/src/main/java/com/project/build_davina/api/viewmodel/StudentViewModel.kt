package com.project.build_davina.api.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.build_davina.api.repository.StudentRepository
import com.project.build_davina.models.StudentModel

class StudentViewModel : ViewModel() {
    private val studentRepository = StudentRepository()

    fun show(id: String) : LiveData<StudentModel.ShowStudent> {
        return studentRepository.show(id)
    }

    fun update(data: StudentModel.ShowStudent) : LiveData<Boolean> {
        return studentRepository.update(data)
    }
}