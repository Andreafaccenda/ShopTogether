package com.example.speedmarket.ui.impostazioni.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.databinding.ViewHolderCarrelloBinding
import com.example.speedmarket.databinding.ViewHolderOrdiniBinding
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Prodotto
import java.text.SimpleDateFormat
import java.util.*


class OrdiniAdapter() : RecyclerView.Adapter<OrdiniAdapter.OrdiniViewHolder>() {


    private var list: MutableList<Carrello> = arrayListOf()
    var onItemClick : ((Carrello) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdiniViewHolder {
        val itemView =
            ViewHolderOrdiniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrdiniViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrdiniViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    fun updateList(list: MutableList<Carrello>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class OrdiniViewHolder(val binding: ViewHolderOrdiniBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Carrello) {
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())
            binding.txtStatoOrdine.text="Stato: ${item.stato}"
            binding.txtData.text="Data:${currentDate}"
            binding.txtPrezzo.text="€ ${item.prezzo}"
        }
    }

}
