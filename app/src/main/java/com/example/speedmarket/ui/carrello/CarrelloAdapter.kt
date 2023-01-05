package com.example.speedmarket.ui.carrello

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.speedmarket.databinding.ViewHolderCarrelloBinding
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.ui.catalogo.ProdViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat

class CarrelloAdapter(): RecyclerView.Adapter<CarrelloAdapter.CarrelloViewHolder>() {


    private var list: MutableList<Prodotto> = arrayListOf()
    var onItemClick : ((Prodotto) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarrelloViewHolder {
        val itemView =
            ViewHolderCarrelloBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarrelloViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CarrelloViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)

        holder.itemView.setOnClickListener{
            holder.binding.piu.setOnClickListener {
                if(item.unita_ordinate < item.disponibilita){
                    item.unita_ordinate+=1
                    holder.binding.quantita.setText(item.unita_ordinate.toString())
                    onItemClick?.invoke(item)
                    }else{
                        holder.binding.quantita.isEnabled=false
                }
            }
            holder.binding.meno.setOnClickListener {
                if(item.unita_ordinate > 1){
                    item.unita_ordinate-=1
                    holder.binding.quantita.setText(item.unita_ordinate.toString())
                    onItemClick?.invoke(item)
                }else{
                    holder.binding.quantita.isEnabled=false
                }

            }
        }
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
            binding.nomeProdotto.setText(item.nome)
            binding.quantita.setText(item.unita_ordinate.toString())
            binding.prezzoTotale.setText(item.offerta?.let {
                calcolaPrezzo(item.prezzo_unitario,item.quantita,
                    it,item.unita_ordinate
                )+"€"
            })
        }
    }


    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri)
        }
    }
    fun calcolaPrezzo(prezzo_unitario:Float, quantita:Float, offerta:Float,unita_ordinate:Int): String {
        val dec = DecimalFormat("#.##")
        return if(offerta < 1) {
            dec.roundingMode = RoundingMode.DOWN
            val prezzo = dec.format(prezzo_unitario * quantita * offerta*unita_ordinate)
            prezzo

        }else{
            dec.roundingMode = RoundingMode.DOWN
            val prezzo = dec.format(prezzo_unitario * quantita*unita_ordinate)
            prezzo
        }

    }

}


