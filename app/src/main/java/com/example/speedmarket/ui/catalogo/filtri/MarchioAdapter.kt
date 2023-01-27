package com.example.speedmarket.ui.catalogo.filtri

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ViewHolderFiltriMarchioBinding

class MarchioAdapter : RecyclerView.Adapter<MarchioAdapter.FiltriViewHolder>() {

    var onItemClick : ((String) -> Unit)? = null
    private  var listaMarchio: ArrayList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltriViewHolder {
        val itemView = ViewHolderFiltriMarchioBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FiltriViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: FiltriViewHolder, position: Int) {
        val currentItem = listaMarchio[position]
        holder.marchio.text= currentItem

        holder.checkMarchio.setOnClickListener {
            holder.checkMarchio.isChecked=false
            onItemClick?.invoke(currentItem)
        }

    }
    override fun getItemCount(): Int {
        return listaMarchio.size
    }
    inner class FiltriViewHolder(val binding: ViewHolderFiltriMarchioBinding) : RecyclerView.ViewHolder(binding.root) {
        val marchio : TextView = itemView.findViewById(R.id.txt_marchio)
        val checkMarchio : RadioButton = itemView.findViewById(R.id.radio_marchio)

    }
    fun updateList(list: ArrayList<String>){
        this.listaMarchio = list
        notifyDataSetChanged()
    }

}