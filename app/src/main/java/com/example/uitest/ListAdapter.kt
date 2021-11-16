package com.example.uitest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uitest.ListAdapter.*

class ListAdapter constructor(var list:ArrayList<Int>): RecyclerView.Adapter<MyViewHolder>() {



    class MyViewHolder constructor(view:View): RecyclerView.ViewHolder(view) {
        var name:TextView
        init {
            name = view.findViewById(R.id.name);
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = "序号:${list.get(position)}"
    }

    override fun getItemCount(): Int {
        return list.size
    }

}