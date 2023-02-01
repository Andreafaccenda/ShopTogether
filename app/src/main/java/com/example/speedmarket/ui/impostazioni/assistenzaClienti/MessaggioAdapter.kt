package com.example.speedmarket.ui.impostazioni.assistenzaClienti


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.model.Messaggio
import com.example.speedmarket.util.Constants.CLICK_ID
import com.example.speedmarket.util.Constants.RECEIVE_ID
import com.example.speedmarket.util.Constants.SEND_ID

class MessaggioAdapter : RecyclerView.Adapter<MessaggioAdapter.MessaggioViewHolder>() {


    var messagesList = mutableListOf<Messaggio>()
    var onItemClick : ((String) -> Unit)? = null

    inner class MessaggioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        var message =itemView.findViewById<TextView>(R.id.tv_message)
        var message_box =itemView.findViewById<TextView>(R.id.tv_bot__message)
        var message_toClick = itemView.findViewById<TextView>(R.id.rv_message_toClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessaggioViewHolder {
        return MessaggioViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.messaggi_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MessaggioViewHolder, position: Int) {
        val currentMessage = messagesList[position]
        holder.message_toClick.setOnClickListener{
            onItemClick?.invoke(currentMessage.testo)
        }
        when (currentMessage.id) {
            SEND_ID -> {
                holder.message.apply {
                    text = currentMessage.testo
                    visibility = View.VISIBLE
                }
                holder.message_box.visibility = View.GONE
                holder.message_toClick.visibility=View.GONE
            }
            RECEIVE_ID -> {
                holder.message_box.apply {
                    text = currentMessage.testo
                    visibility = View.VISIBLE
                }
                holder.message.visibility = View.GONE
                holder.message_toClick.visibility=View.GONE
            }
            CLICK_ID->{
                holder.message_toClick.apply {
                    text = currentMessage.testo
                    visibility = View.VISIBLE
                }
                holder.message.visibility = View.GONE
                holder.message_box.visibility=View.GONE
            }

        }
    }


    override fun getItemCount(): Int {
        return messagesList.size
    }

    fun insertMessage(message: Messaggio){
        this.messagesList.add(message)
        notifyItemInserted(messagesList.size)
        notifyDataSetChanged()
    }
}