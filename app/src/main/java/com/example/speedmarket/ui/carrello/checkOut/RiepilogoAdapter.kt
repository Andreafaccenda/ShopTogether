package com.example.speedmarket.ui.carrello.checkOut

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.databinding.ViewHolderRiepilogoCarrelloBinding
import com.example.speedmarket.model.Prodotto
import java.math.RoundingMode
import java.text.DecimalFormat


class RiepilogoAdapter(): RecyclerView.Adapter<RiepilogoAdapter.RiepilogoCarrelloViewHolder>(){


    private var list: MutableList<Prodotto> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiepilogoCarrelloViewHolder {
        val itemView = ViewHolderRiepilogoCarrelloBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RiepilogoCarrelloViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RiepilogoCarrelloViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)

    }

     fun updateList(list: MutableList<Prodotto>){
        this.list = list
        notifyDataSetChanged()
    }



    override fun getItemCount(): Int {
        return list.size
    }

    inner class RiepilogoCarrelloViewHolder(val binding: ViewHolderRiepilogoCarrelloBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Prodotto){

            binding.txtNomeProdotto.text=item.nome
            binding.txtQuantit.text=item.unita_ordinate.toString()
            binding.txtPrezzo.text=
                item.offerta?.let {
                    calcolaPrezzo(item.prezzo_unitario,item.quantita,
                        it,item.unita_ordinate)
                }
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


