package com.project.build_davina.api.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.build_davina.api.global.Config
import com.project.build_davina.api.global.Data
import com.project.build_davina.models.StudentModel

class StudentRepository {
    private val firestore = Config.firestore

    fun show(id: String) : LiveData<StudentModel.ShowStudent> {
        val result = MutableLiveData<StudentModel.ShowStudent>()

        firestore.collection(Data.student)
            .document(id)
            .get()
            .addOnSuccessListener { std ->
                val name = std.get("name").toString()
                val nim = std.get("nim").toString()

                firestore.collection(Data.user)
                    .document(id)
                    .get()
                    .addOnSuccessListener { user ->
                        val email = user.get("email").toString()
                        val username = user.get("username").toString()

                        val data = StudentModel.ShowStudent (
                            id,
                            name,
                            nim,
                            email,
                            username
                        )

                        result.value = data
                    }
            }
        return result
    }

    fun update(data: StudentModel.ShowStudent) : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        firestore.collection(Data.student)
            .document(data.id)
            .update(
                "name", data.name,
                "nim", data.nim
            )
            .addOnSuccessListener {
                firestore.collection(Data.user)
                    .document(data.id)
                    .update(
                        "email", data.email,
                        "username", data.username
                    )
                    .addOnSuccessListener {
                        result.value = true
                    }
            }
        return result
    }
}