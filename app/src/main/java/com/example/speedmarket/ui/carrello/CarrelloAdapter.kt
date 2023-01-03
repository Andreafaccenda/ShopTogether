package com.example.speedmarket.ui.carrello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.speedmarket.databinding.ViewHolderCarrelloBinding
import com.example.speedmarket.databinding.ViewHolderProdottoBinding
import com.example.speedmarket.model.Categorie
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.ui.catalogo.ProdottoAdapter
import com.example.speedmarket.ui.catalogo.ProdottoAdapterInt
import com.example.speedmarket.util.hide
import java.math.RoundingMode
import java.text.DecimalFormat

class CarrelloAdapter(): RecyclerView.Adapter<CarrelloAdapter.CarrelloViewHolder>() {


    private var list: MutableList<Prodotto> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarrelloViewHolder {
        val itemView =
            ViewHolderCarrelloBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarrelloViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CarrelloViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)


    }

    fun updateList(list: MutableList<Prodotto>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CarrelloViewHolder(val binding: ViewHolderCarrelloBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Prodotto) {
            bindImage(binding.imageProdotto, item.immagine)
        }
    }


    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri)
        }
    }


}


