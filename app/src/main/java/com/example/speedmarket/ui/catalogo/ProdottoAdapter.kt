package com.example.speedmarket.ui.catalogo

import android.annotation.SuppressLint
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


class ProdottoAdapter : RecyclerView.Adapter<ProdottoAdapter.ProdottoViewHolder>(),
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
    override fun removeItem(id: String){
        val listaAggiornata: MutableList<Prodotto> = arrayListOf()
       for(prodotto in this.list){
           if(prodotto.id == id){
                listaAggiornata.remove(prodotto)
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

    override fun filtraListaPrezzo(prezzo: String, list: MutableList<Prodotto>) {
        val listaAggiornata: MutableList<Prodotto> = arrayListOf()
        for (prodotto in list) {
            val price =(prodotto.quantita*prodotto.prezzo_unitario*prodotto.offerta!!)
            if (price<=prezzo.toFloat()) {
                listaAggiornata.add(prodotto)
            }
        }
        updateList(listaAggiornata)
    }
    override fun filtraListaMarchio(marchio: String, list: MutableList<Prodotto>) {

        val listaAggiornata: MutableList<Prodotto> = arrayListOf()
        for (prodotto in list) {
            if (prodotto.produttore.equals(marchio, true)) {
                listaAggiornata.add(prodotto)
            }
        }
        updateList(listaAggiornata)

    }

    override fun filtraListaSottoCategoria(subCategoria: String, list: MutableList<Prodotto>) {
        val listaAggiornata: MutableList<Prodotto> = arrayListOf()
        for (prodotto in list) {
            if (prodotto.sub_categoria.equals(subCategoria, true)) {
                listaAggiornata.add(prodotto)
            }
        }
        updateList(listaAggiornata)

    }

    override fun filtraListaMarchioPrezzo(prezzo: String, marchio: String, list: MutableList<Prodotto>) {
        val listaAggiornata: MutableList<Prodotto> = arrayListOf()
        for (prodotto in list) {
            if (prodotto.produttore.equals(marchio, true)) {
                val price =(prodotto.quantita*prodotto.prezzo_unitario*prodotto.offerta!!)
                if (price<=prezzo.toFloat()) {
                    listaAggiornata.add(prodotto)
                }
            }
        }
        updateList(listaAggiornata)

    }

    override fun filtraListaCategoriaPrezzo(prezzo: String, subCategoria: String, list: MutableList<Prodotto>) {
        val listaAggiornata: MutableList<Prodotto> = arrayListOf()
        for (prodotto in list) {
            if (prodotto.sub_categoria.equals(subCategoria, true)) {
                val price =(prodotto.quantita*prodotto.prezzo_unitario*prodotto.offerta!!)
                if (price<=prezzo.toFloat()) {
                    listaAggiornata.add(prodotto)
                }
            }
        }
        updateList(listaAggiornata)
    }

    override fun filtraListaCategoriaMarchio(marchio: String, subCategoria: String, list: MutableList<Prodotto>) {
        val listaAggiornata: MutableList<Prodotto> = arrayListOf()
        for (prodotto in list) {
            if (prodotto.sub_categoria.equals(subCategoria, true)) {
                if (prodotto.produttore.equals(marchio, true)) {
                    listaAggiornata.add(prodotto)
                }
            }
        }
        updateList(listaAggiornata)
    }

    override fun filtraLista(prezzo: String, marchio: String, subCategoria: String, list: MutableList<Prodotto>) {
        val listaAggiornata: MutableList<Prodotto> = arrayListOf()
        for (prodotto in list) {
            if (prodotto.sub_categoria.equals(subCategoria, true)) {
                val price =(prodotto.quantita*prodotto.prezzo_unitario*prodotto.offerta!!)
                if (price<=prezzo.toFloat()) {
                    if (prodotto.produttore.equals(marchio, true)) {
                        listaAggiornata.add(prodotto)
                    }
                }
            }
        }
        updateList(listaAggiornata)

    }

    override fun filtraListaOfferta(list: MutableList<Prodotto>) {
        val listaAggiornata: MutableList<Prodotto> = arrayListOf()
        for (prodotto in list) {
            if (prodotto.offerta!! < 1) {
                listaAggiornata.add(prodotto)
            }
        }
        updateList(listaAggiornata)
    }

    override fun filtraListaqrCode(qrCode: String, list: MutableList<Prodotto>) {
        val listaAggiornata: MutableList<Prodotto> = arrayListOf()
        for (prodotto in list) {
            if (prodotto.id == qrCode) {
                listaAggiornata.add(prodotto)
            }
        }
        updateList(listaAggiornata)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ProdottoViewHolder(val binding: ViewHolderProdottoBinding ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Prodotto){
            bindImage(binding.imageProdotto, item.immagine)
            binding.txtProdotto.text=item.nome
            binding.txtQuantit.text=item.quantita.toString()+"Kg"
            binding.txtPrezzo.text=item.prezzo_unitario.toString()+"€/Kg"
            if(item.offerta!! < 1){
                binding.txtPrezzoUnitario.text="Offerta:"
                binding.txtPrezzoOfferta.text=(calcolaPrezzo(item.prezzo_unitario,item.quantita,
                    item.offerta!!)+"€")
                binding.txtPrezzoOffertaSenzaSconto.text=(calcolaPrezzo(item.prezzo_unitario,item.quantita,
                    1.0F
                )+"€")
            }else {
                binding.txtPrezzoUnitario.hide()
                binding.txtPrezzoOfferta.text=(calcolaPrezzo(item.prezzo_unitario,item.quantita,
                    item.offerta!!)+"€")
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
        val dec = DecimalFormat("##0.00")
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

