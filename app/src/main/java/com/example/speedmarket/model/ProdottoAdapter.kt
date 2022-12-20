package com.example.speedmarket.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ViewHolderProdottoBinding
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
    fun filterList(filtro : String,list: MutableList<Prodotto>){
        val lista= mutableListOf<Prodotto>()
        if(filtro.isNotEmpty()){
            for(it in list){
                if (it.nome.toLowerCase(Locale.getDefault()).startsWith(filtro)){
                    lista.add(it)}
                }
            this.list = lista
            notifyDataSetChanged()
            }else{
            this.list = list
            notifyDataSetChanged()}
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
            binding.txtPrezzo.setText(item.prezzo_unitario.toString())
        }
    }
}






/*class ProdottoAdapter(private val List: ArrayList<Prodotto>):
    RecyclerView.Adapter<ProdottoAdapter.ProdottoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdottoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_prodotto,parent,false)
        return ProdottoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProdottoViewHolder, position: Int) {
        val currentItem = List[position]
        //holder.image.setImageResource(currentItem.immagine)
        holder.title.text= currentItem.nome
        holder.prezzo.text= "€ " + currentItem.prezzo_unitario.toString()
    }

    override fun getItemCount(): Int {
        return List.size
    }
    class ProdottoViewHolder(view : View) : RecyclerView.ViewHolder(view){

        //val image : ImageView = itemView.findViewById(R.id.image_prodotto)
        val title : TextView = itemView.findViewById(R.id.txt_prodotto)
        val prezzo : TextView = itemView.findViewById(R.id.txt_prezzo)

    }
}*/