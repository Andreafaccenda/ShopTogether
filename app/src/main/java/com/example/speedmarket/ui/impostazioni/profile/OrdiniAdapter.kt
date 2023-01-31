package com.example.speedmarket.ui.impostazioni.profile

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.databinding.ViewHolderOrdiniBinding
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Carrello.Stato.*


class OrdiniAdapter : RecyclerView.Adapter<OrdiniAdapter.OrdiniViewHolder>() {


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

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(item)
        }
    }

    fun updateList(list: MutableList<Carrello>) {
        this.list = list
        this.list.reverse()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class OrdiniViewHolder(val binding: ViewHolderOrdiniBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Carrello) {
            binding.txtStatoOrdine.text="Stato: ${item.stato}"
            when(item.stato){
                elaborazione ->binding.txtStatoOrdine.setTextColor(Color.RED)
                spedizione ->binding.txtStatoOrdine.setTextColor(Color.parseColor("#FF9800"))
                consegnato ->binding.txtStatoOrdine.setTextColor(Color.GREEN)
                incompleto -> TODO()
            }
            binding.txtData.text="Data:${item.date}"
            binding.txtPrezzo.text="€ ${item.prezzo}"
        }
    }

}
