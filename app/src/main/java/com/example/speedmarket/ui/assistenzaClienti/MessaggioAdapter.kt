package com.example.speedmarket.ui.assistenzaClienti

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.model.Messaggio
import com.example.speedmarket.util.Constants.RECEIVE_ID
import com.example.speedmarket.util.Constants.SEND_ID

class MessaggioAdapter : RecyclerView.Adapter<MessaggioAdapter.MessaggioViewHolder>() {


    var messagesList = mutableListOf<Messaggio>()

    inner class MessaggioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                messagesList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }

        }
        var message =itemView.findViewById<TextView>(R.id.tv_message)
        var message_box =itemView.findViewById<TextView>(R.id.tv_bot__message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessaggioViewHolder {
        return MessaggioViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.messaggi_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MessaggioViewHolder, position: Int) {
        val currentMessage = messagesList[position]
        when (currentMessage.id) {
            SEND_ID -> {
                holder.message.apply {
                    text = currentMessage.messaggio
                    visibility = View.VISIBLE
                }
                holder.message_box.visibility = View.GONE
            }
            RECEIVE_ID -> {
                holder.message_box.apply {
                    text = currentMessage.messaggio
                    visibility = View.VISIBLE
                }
                holder.message.visibility = View.GONE
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