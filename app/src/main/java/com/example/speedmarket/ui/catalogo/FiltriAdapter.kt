package com.example.speedmarket.ui.catalogo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ViewHolderFiltriMarchioBinding
import com.example.speedmarket.model.Prodotto

class FiltriAdapter(): RecyclerView.Adapter<FiltriAdapter.FiltriViewHolder>() {

    var onItemClick : ((String) -> Unit)? = null
    private  var lista_marchio: ArrayList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltriViewHolder {
        val itemView = ViewHolderFiltriMarchioBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FiltriViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: FiltriViewHolder, position: Int) {
        val currentItem = lista_marchio[position]
        holder.marchio.text= currentItem

        holder.check_marchio.setOnClickListener {
            holder.check_marchio.isChecked=false
            onItemClick?.invoke(currentItem)
        }

    }
    override fun getItemCount(): Int {
        return lista_marchio.size
    }
    inner class FiltriViewHolder(val binding: ViewHolderFiltriMarchioBinding) : RecyclerView.ViewHolder(binding.root) {
        val marchio : TextView = itemView.findViewById(R.id.txt_marchio)
        val check_marchio : RadioButton = itemView.findViewById(R.id.radio_marchio)

    }
    fun updateList(list: ArrayList<String>){
        this.lista_marchio = list
        notifyDataSetChanged()
    }

}