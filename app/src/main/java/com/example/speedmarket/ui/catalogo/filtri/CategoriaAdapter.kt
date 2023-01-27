package com.example.speedmarket.ui.catalogo.filtri

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ViewHolderFiltriCategoriaBinding

class CategoriaAdapter : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

        var onItemClick : ((String) -> Unit)? = null
        private  var listaCategorie: ArrayList<String> = arrayListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
            val itemView = ViewHolderFiltriCategoriaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return CategoriaViewHolder(itemView)
        }
        override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
            val currentItem = listaCategorie[position]
            holder.subCategoria.text= currentItem

           holder.checkMarchio.setOnClickListener {
               holder.checkMarchio.isChecked=false
                onItemClick?.invoke(currentItem)
            }

        }
        override fun getItemCount(): Int {
            return listaCategorie.size
        }
        inner class CategoriaViewHolder(val binding: ViewHolderFiltriCategoriaBinding) : RecyclerView.ViewHolder(binding.root) {
            val subCategoria : TextView = itemView.findViewById(R.id.sub_categorie)
            val checkMarchio : RadioButton = itemView.findViewById(R.id.radio_subCategorie)

        }
        fun updateList(list: ArrayList<String>){
            this.listaCategorie = list
            notifyDataSetChanged()
        }

    }