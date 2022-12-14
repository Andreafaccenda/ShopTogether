package com.example.speedmarket.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.model.Categorie

class CategorieAdapter(private val List: ArrayList<Categorie>):
    RecyclerView.Adapter<CategorieAdapter.CategorieViewHolder>() {

    var onItemClick : ((Categorie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategorieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_categorie,parent,false)
        return CategorieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategorieViewHolder, position: Int) {
       val currentItem = List[position]
        holder.titleImage.setImageResource(currentItem.immagine)
        holder.txt_categoria.text= currentItem.title
        holder.background.setBackgroundResource(currentItem.sfondo)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return List.size
    }
    class CategorieViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val titleImage : ImageView = itemView.findViewById(R.id.image_category)
        val txt_categoria : TextView = itemView.findViewById(R.id.title_category)
        val background : ConstraintLayout = itemView.findViewById(R.id.mainLayout)

    }
}

