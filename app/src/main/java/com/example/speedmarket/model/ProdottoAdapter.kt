package com.example.speedmarket.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ViewHolderProdottoBinding
import com.example.speedmarket.util.hide
import java.text.DecimalFormat
import java.util.*


class ProdottoAdapter(): RecyclerView.Adapter<ProdottoAdapter.ProdottoViewHolder>() {


    private var list: MutableList<Prodotto> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdottoViewHolder {
        val itemView = ViewHolderProdottoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProdottoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProdottoViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

   fun updateList(list: MutableList<Prodotto>){
        this.list = list
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        list.removeAt(position)
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ProdottoViewHolder(val binding: ViewHolderProdottoBinding ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Prodotto){
            binding.txtProdotto.setText(item.nome)
            binding.txtQuantit.setText(item.quantita.toString()+"Kg")
            binding.txtPrezzo.setText(item.prezzo_unitario.toString()+"€/Kg")
            if(item.offerta!! < 1){
                binding.txtPrezzoUnitario.setText("Offerta:")
                binding.txtPrezzoOfferta.setText(prezzo_unitario(item.prezzo_unitario,item.quantita,item.offerta)+"€")
                binding.txtPrezzoOffertaSenzaSconto.setText(prezzo_unitario(item.prezzo_unitario,item.quantita,
                    1.0F
                )+"€")
            }else {
                binding.txtPrezzoUnitario.hide()
                binding.txtPrezzoOfferta.setText(prezzo_unitario(item.prezzo_unitario,item.quantita,item.offerta)+"€")
                binding.txtPrezzoOffertaSenzaSconto.hide()
                binding.sbarraOfferta.hide()
            }

        }
    }
    fun prezzo_unitario(prezzo:Float,quantità:Float,offerta:Float):String{
        if(offerta < 1) {
            val dec = DecimalFormat("#.##")
            var result = dec.format(prezzo * quantità * offerta)
            return result
        }else{
            val dec = DecimalFormat("#.##")
            var result = dec.format(prezzo * quantità)
            return result
        }

    }

}

