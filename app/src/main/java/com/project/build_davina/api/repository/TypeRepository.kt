package com.project.build_davina.api.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.build_davina.api.global.Config
import com.project.build_davina.api.global.Data
import com.project.build_davina.models.TypeModel

class TypeRepository {
    private val firestore = Config.firestore

    fun getList(callback: (List<TypeModel>?, Exception?) -> Unit) {
        firestore.collection(Data.type).get()
            .addOnSuccessListener { querySnapshot ->
                val dataList = mutableListOf<TypeModel>()
                for (document in querySnapshot.documents) {
                    val id = document.id
                    val name = document.getString("name") ?: ""
                    dataList.add(TypeModel(id, name))
                }
                callback(dataList, null)
            }
            .addOnFailureListener { exception ->
                callback(null, exception)
            }
    }

    fun load() : LiveData<List<TypeModel>> {
        val resultLiveData = MutableLiveData<List<TypeModel>>()

        firestore.collection(Data.type)
            .get()
            .addOnSuccessListener { docs ->
                val dataList = mutableListOf<TypeModel>()
                for (doc in docs) {
                    val data = TypeModel(
                        doc.get("id").toString(),
                        doc.get("name").toString()
                    )
                    dataList.add(data)
                }
                resultLiveData.value = dataList
            }

        return resultLiveData
    }

    fun create(data: TypeModel) : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        val hm = HashMap<String,Any>()
        hm.set("id", data.id)
        hm.set("name", data.name)

        firestore.collection(Data.type)
            .document(data.id)
            .set(hm)
            .addOnSuccessListener { result.value = true }
            .addOnFailureListener { result.value = false }

        return result
    }

    fun update(data: TypeModel) : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        val hm = HashMap<String,Any>()
        hm.set("id", data.id)
        hm.set("name", data.name)

        firestore.collection(Data.type)
            .document(data.id)
            .update(hm)
            .addOnSuccessListener { result.value = true }
            .addOnFailureListener { result.value = false }

        return result
    }

    fun delete(id: String) : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        firestore.collection(Data.type)
            .document(id)
            .delete()
            .addOnSuccessListener { result.value = true }
            .addOnFailureListener { result.value = false }

        return result
    }
}