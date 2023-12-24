package com.project.build_davina.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.build_davina.R
import com.project.build_davina.models.TypeModel
import com.project.build_davina.views.admin.type.TypeActivity

class TypeAdapter(private var dataList: List<TypeModel>, private val listener : TypeActivity) :
    RecyclerView.Adapter<TypeAdapter.HolderData>(){
    class HolderData (v : View) : RecyclerView.ViewHolder(v) {
        val cd = v.findViewById<RelativeLayout>(R.id.it_container)
        val num = v.findViewById<TextView>(R.id.it_number)
        val name = v.findViewById<TextView>(R.id.it_criteria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderData {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_base, parent, false)
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
        holder.cd.setOnClickListener {
            listener.selector(data.id, data.name)
        }
    }

    fun setData(newDataList: List<TypeModel>) {
        dataList = newDataList
        notifyDataSetChanged()
    }


}