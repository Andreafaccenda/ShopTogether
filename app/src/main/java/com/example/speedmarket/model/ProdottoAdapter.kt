package com.example.speedmarket.model

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.speedmarket.databinding.ViewHolderProdottoBinding
import com.example.speedmarket.util.hide
import java.text.DecimalFormat


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
    fun filtraLista(tipo: String,list: MutableList<Prodotto>) {

        val lista_aggiornata: MutableList<Prodotto> = arrayListOf()
            for (prodotto in list) {
                if (prodotto.categoria.equals(tipo, true)) {
                    lista_aggiornata.add(prodotto)
                }
            }
            updateList(lista_aggiornata)

    }
    override fun getItemCount(): Int {
        return list.size
    }

    inner class ProdottoViewHolder(val binding: ViewHolderProdottoBinding ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Prodotto){
            bindImage(binding.imageProdotto, item.immagine)
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

    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri)
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

