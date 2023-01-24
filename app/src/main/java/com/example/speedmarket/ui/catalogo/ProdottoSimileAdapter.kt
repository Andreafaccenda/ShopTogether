package com.example.speedmarket.ui.catalogo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.speedmarket.databinding.ViewHolderProdottoSimileBinding
import com.example.speedmarket.model.Prodotto

class ProdottoSimileAdapter() : RecyclerView.Adapter<ProdottoSimileAdapter.ProdottoSimileViewHolder>() {


    private var list: MutableList<Prodotto> = arrayListOf()
    var onItemClick : ((Prodotto) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdottoSimileViewHolder {
        val itemView = ViewHolderProdottoSimileBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProdottoSimileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProdottoSimileViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(item)
        }
    }

     fun updateList(list: MutableList<Prodotto>){
        this.list = list
        notifyDataSetChanged()
    }

     fun filtraListaCategoria(tipo: String, list: MutableList<Prodotto>) {
        val listaAggiornata: MutableList<Prodotto> = arrayListOf()
        for (prodotto in list) {
            if (prodotto.categoria.equals(tipo, true)) {
                listaAggiornata.add(prodotto)
            }
        }
        updateList(listaAggiornata)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ProdottoSimileViewHolder(val binding: ViewHolderProdottoSimileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Prodotto) {
            bindImage(binding.imageProdotto, item.immagine)
            binding.nomeProdotto.text = item.nome

        }

        fun bindImage(imgView: ImageView, imgUrl: String?) {
            imgUrl?.let {
                val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                imgView.load(imgUri)
            }
        }


    }

}