package com.project.build_davina.api.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.build_davina.api.global.Config
import com.project.build_davina.api.global.Data
import com.project.build_davina.models.CriteriaModel

class CriteriaRepository {
    private val firestore = Config.firestore

    fun getList(callback: (List<CriteriaModel>?, Exception?) -> Unit) {
        firestore.collection(Data.criteria).get()
            .addOnSuccessListener { querySnapshot ->
                val dataList = mutableListOf<CriteriaModel>()
                for (document in querySnapshot.documents) {
                    val id = document.id
                    val name = document.getString("name") ?: ""
                    dataList.add(CriteriaModel(id, name))
                }
                callback(dataList, null)
            }
            .addOnFailureListener { exception ->
                callback(null, exception)
            }
    }

    fun load() : LiveData<List<CriteriaModel>> {
        val result = MutableLiveData<List<CriteriaModel>>()

        firestore.collection(Data.criteria).get()
            .addOnSuccessListener { documents ->
                val data = mutableListOf<CriteriaModel>()
                for (document in documents) {
                    val id = document.id
                    val name = document.data["name"].toString()

                    data.add(CriteriaModel(id, name))
                }
                result.value = data
            }
            .addOnFailureListener { result.value = listOf() }

        return result
    }

    fun create(data: CriteriaModel) : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        val hm = HashMap<String, String>()
        hm.set("id", data.id)
        hm.set("name", data.name)

        firestore.collection(Data.criteria).document(data.id).set(hm)
            .addOnSuccessListener { result.value = true }
            .addOnFailureListener { result.value = false }

        return result
    }

    fun update(data: CriteriaModel) : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        val hm = HashMap<String, Any>()
        hm.set("id", data.id)
        hm.set("name", data.name)

        firestore.collection(Data.criteria).document(data.id).update(hm)
            .addOnSuccessListener { result.value = true }
            .addOnFailureListener { result.value = false }

        return result
    }

    fun delete(id: String) : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        firestore.collection(Data.criteria).document(id).delete()
            .addOnSuccessListener { result.value = true }
            .addOnFailureListener { result.value = false }

        return result
    }
}