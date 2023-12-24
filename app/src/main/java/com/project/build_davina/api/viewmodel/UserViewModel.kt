package com.project.build_davina.api.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.build_davina.api.repository.UserRepository
import com.project.build_davina.models.StudentModel
import com.project.build_davina.models.UserModel

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()

    fun registerUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        userRepository.createUserWithEmailAndPassword(email, password, onComplete)
    }

    fun createUser(user: UserModel): LiveData<Boolean> {
        return userRepository.create(user)
    }

    fun createSingle(data: StudentModel.Student, coll: String): LiveData<Boolean> {
        return userRepository.createSingle(data, coll)
    }
}