package com.project.build_davina.models

class StudentModel {
    data class ShowStudent (
        val id: String,
        val name: String,
        val nim: String,
        val email: String,
        val username: String
    )

    data class Student (
        val id: String,
        val name: String,
        val nim: String,
    )
}
