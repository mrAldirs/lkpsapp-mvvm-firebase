package com.project.build_davina.models


class DocumentModel {

    data class Document (
        val id: String,
        val name: String,
        val criteria_id: String,
        val type_id: String,
        val desk: String,
        val additional_information: String,
        val created_at: String,
    )

    data class ShowDocument (
        val id: String,
        val name: String,
        val criteria_id: String,
        val type_id: String,
        val criteria_name: String,
        val type_name: String,
        val desk: String,
        val additional_information: String,
        val file: String,
        val created_at: String,
    )

    data class ChartDocument (
        val crated_at: String,
        val count: Long
    )
}