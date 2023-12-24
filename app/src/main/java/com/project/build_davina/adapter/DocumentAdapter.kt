package com.project.build_davina.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.build_davina.R
import com.project.build_davina.models.DocumentModel
import com.project.build_davina.views.admin.document.DocumentActivity
import com.project.build_davina.views.admin.document.ShowFragment

class DocumentAdapter(private var dataList: List<DocumentModel.Document>, val listener: DocumentActivity) :
    RecyclerView.Adapter<DocumentAdapter.HolderData>(){
    class HolderData (v : View) : RecyclerView.ViewHolder(v) {
        val cd = v.findViewById<RelativeLayout>(R.id.it_container)
        val num = v.findViewById<TextView>(R.id.it_number)
        val name = v.findViewById<TextView>(R.id.it_name)
        val desk = v.findViewById<TextView>(R.id.it_desk)
        val created = v.findViewById<TextView>(R.id.it_created)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderData {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_document, parent, false)
        return HolderData(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HolderData, position: Int) {
        val data = dataList.get(position)
        holder.num.text = "${position + 1}"
        holder.name.text = data.name
        holder.desk.text = "Description: ${data.desk}"
        holder.created.text = data.created_at
        holder.cd.setOnClickListener {
            val frag = ShowFragment()

            val bundle = Bundle()
            bundle.putString("id", data.id)
            frag.arguments = bundle

            frag.show(listener.supportFragmentManager, "show")
        }
    }

    fun setData(newDataList: List<DocumentModel.Document>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}