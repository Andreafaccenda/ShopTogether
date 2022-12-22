package com.example.speedmarket.ui.catalogo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.speedmarket.databinding.ViewHolderProdottoBinding
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.util.hide
import java.math.RoundingMode
import java.text.DecimalFormat


class ProdottoAdapter(): RecyclerView.Adapter<ProdottoAdapter.ProdottoViewHolder>(),
    ProdottoAdapterInt {


    private var list: MutableList<Prodotto> = arrayListOf()
    var onItemClick : ((Prodotto) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdottoViewHolder {
        val itemView = ViewHolderProdottoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProdottoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProdottoViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(item)
        }
    }

   override fun updateList(list: MutableList<Prodotto>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun filtraListaCategoria(tipo: String, list: MutableList<Prodotto>) {
        val listaAggiornata: MutableList<Prodotto> = arrayListOf()
            for (prodotto in list) {
                if (prodotto.categoria.equals(tipo, true)) {
                    listaAggiornata.add(prodotto)
                }
            }
            updateList(listaAggiornata)

    }

    override fun filtraListaNomeChange(tipo: String, list: MutableList<Prodotto>) {
        val listaAggiornata: MutableList<Prodotto> = arrayListOf()
        for (prodotto in list) {
            if (prodotto.nome.startsWith(tipo, true)) {
                listaAggiornata.add(prodotto)
            }
        }
        updateList(listaAggiornata)

    }

    override fun filtraListaNome(tipo: String, list: MutableList<Prodotto>) {

        val listaAggiornata: MutableList<Prodotto> = arrayListOf()
        for (prodotto in list) {
            if (prodotto.nome.contains(tipo, true)) {
                listaAggiornata.add(prodotto)
            }
        }
        updateList(listaAggiornata)

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
                binding.txtPrezzoOfferta.setText((calcolaPrezzo(item.prezzo_unitario,item.quantita,item.offerta)+"€"))
                binding.txtPrezzoOffertaSenzaSconto.setText(calcolaPrezzo(item.prezzo_unitario,item.quantita,
                    1.0F
                )+"€")
            }else {
                binding.txtPrezzoUnitario.hide()
                binding.txtPrezzoOfferta.setText(calcolaPrezzo(item.prezzo_unitario,item.quantita,item.offerta)+"€")
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

    fun calcolaPrezzo(prezzo_unitario:Float, quantita:Float, offerta:Float): String {
        val dec = DecimalFormat("#.##")
        return if(offerta < 1) {
            dec.roundingMode = RoundingMode.DOWN
            val prezzo = dec.format(prezzo_unitario * quantita * offerta)
            prezzo

        }else{
            dec.roundingMode = RoundingMode.DOWN
            val prezzo = dec.format(prezzo_unitario * quantita)
            prezzo
        }

    }

}

