package com.project.build_davina.api.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.build_davina.api.global.Config
import com.project.build_davina.api.global.Data
import com.project.build_davina.models.DocumentModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class DocumentRepository {
    private val firestore = Config.firestore
    private val storage = Config.storage

    fun getChart(): LiveData<List<DocumentModel.ChartDocument>> {
        val resultLiveData = MutableLiveData<List<DocumentModel.ChartDocument>>()

        firestore.collection(Data.document)
            .get()
            .addOnSuccessListener { docs ->
                val groupedDataMap = mutableMapOf<String, Long>()

                for (doc in docs) {
                    val create = doc.get("created_at").toString()

                    if (groupedDataMap.containsKey(create)) {
                        groupedDataMap[create] = groupedDataMap[create]!! + 1
                    } else {
                        groupedDataMap[create] = 1
                    }
                }

                val dataList = groupedDataMap.map { entry ->
                    DocumentModel.ChartDocument(entry.key.substring(0, minOf(entry.key.length, 10)), entry.value)
                }
                val sortedDataList = dataList.sortedByDescending { it.count }.take(5)

                resultLiveData.value = sortedDataList
            }

        return resultLiveData
    }

    fun create(data: DocumentModel.Document, uri: Uri) : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        val hm = HashMap<String, Any>()
        hm.set("id", data.id)
        hm.set("name", data.name)
        hm.set("criteria_id", data.criteria_id)
        hm.set("type_id", data.type_id)
        hm.set("desk", data.desk)
        hm.set("additional_information", data.additional_information)
        hm.set("created_at", data.created_at)

        if (uri != Uri.EMPTY) {
            val fileName = "PDF ${SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Date())}"
            val ref = storage.reference.child("${fileName}.pdf")
            ref.putFile(uri)
                .addOnSuccessListener {
                    ref.downloadUrl
                        .addOnSuccessListener { uri ->
                            hm.set("file", uri.toString())
                            firestore.collection(Data.document)
                                .document(data.id)
                                .set(hm)
                                .addOnSuccessListener { result.value = true }
                                .addOnFailureListener { result.value = false }
                        }
                        .addOnFailureListener { result.value = false }
                }
                .addOnFailureListener { result.value = false }
        } else {
            firestore.collection(Data.document)
                .document(data.id)
                .set(hm)
                .addOnSuccessListener { result.value = true }
                .addOnFailureListener { result.value = false }
        }

        return result
    }

    fun load(filter: String?): LiveData<List<DocumentModel.Document>> {
        val result = MutableLiveData<List<DocumentModel.Document>>()

        val query = if (filter != null) {
            firestore.collection(Data.document)
                .whereEqualTo("criteria_id", filter)
        } else {
            firestore.collection(Data.document)
        }

        query.get()
            .addOnSuccessListener { documents ->
                val data = mutableListOf<DocumentModel.Document>()
                for (document in documents) {
                    val id = document.id
                    val name = document.data["name"].toString()
                    val criteria_id = document.data["criteria_id"].toString()
                    val type_id = document.data["type_id"].toString()
                    val desk = document.data["desk"].toString()
                    val additional_information = document.data["additional_information"].toString()
                    val created_at = document.data["created_at"].toString()

                    data.add(DocumentModel.Document(id, name, criteria_id, type_id, desk, additional_information, created_at))
                }
                result.value = data
            }
            .addOnFailureListener { result.value = listOf() }

        return result
    }

    fun show(id: String) : LiveData<DocumentModel.ShowDocument> {
        val result = MutableLiveData<DocumentModel.ShowDocument>()

        firestore.collection(Data.document)
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                val id = document.id
                val name = document.data?.get("name").toString()
                val criteria_id = document.data?.get("criteria_id").toString()
                val type_id = document.data?.get("type_id").toString()
                val desk = document.data?.get("desk").toString()
                val additional_information = document.data?.get("additional_information").toString()
                val created_at = document.data?.get("created_at").toString()
                val file = document.data?.get("file").toString()

                firestore.collection(Data.criteria).whereEqualTo("id", criteria_id)
                    .get()
                    .addOnSuccessListener { criterias ->
                        for (criteria in criterias) {
                            val criteria_name = criteria.data["name"].toString()

                            firestore.collection(Data.type).whereEqualTo("id", type_id)
                                .get()
                                .addOnSuccessListener { types ->
                                    for (type in types) {
                                        val type_name = type.data["name"].toString()
                                        result.value = DocumentModel.ShowDocument(
                                            id,
                                            name,
                                            criteria_id,
                                            type_id,
                                            criteria_name,
                                            type_name,
                                            desk,
                                            additional_information,
                                            file,
                                            created_at
                                        )
                                    }
                                }
                                .addOnFailureListener { result.value = DocumentModel.ShowDocument("", "", "", "", "", "", "", "", "","") }
                        }
                    }
            }
        return result
    }

    fun delete(id: String) : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        firestore.collection(Data.document)
            .document(id)
            .delete()
            .addOnSuccessListener { result.value = true }
            .addOnFailureListener { result.value = false }

        return result
    }
}