package com.example.food_appadmin.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_appadmin.databinding.PendingorderRvItemBinding

class pendingorderAdapter(
    private val context: Context,
    private val customernames: MutableList<String>,
    private val images: MutableList<String>,
    private val quantity: MutableList<String>,
    private val itemClicked: OnItemClicked,

    ) : RecyclerView.Adapter<pendingorderAdapter.pendingorderViewholder>() {
    interface OnItemClicked {
        fun OnItemClickListener(position: Int)
        fun OnItemAcceptListener(position: Int)
        fun OnItemDispatchListener(position: Int)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): pendingorderViewholder {
        val binding =
            PendingorderRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return pendingorderViewholder(binding)
    }

    override fun onBindViewHolder(
        holder: pendingorderViewholder,
        position: Int,
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return customernames.size
    }

    inner class pendingorderViewholder(private val binding: PendingorderRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            var isAccepted = false
            binding.apply {
                customername.text = customernames[position]
                val uri = Uri.parse(images[position])
                Glide.with(context).load(uri).into(itemImage)
                itemPrice.text = quantity[position].toString()
            }
            binding.Acceptorderbtn.apply {
                if (!isAccepted) {
                    text = "Accept" /// show accept if it not accepted
                } else {
                    text = "Dispatch" //   show dispatch if it is accepted
                }

                setOnClickListener {
                    if (!isAccepted) { // show Dispatch after accepting the order
                        text = "Dispatch"
                        isAccepted = true
                        Toast.makeText(it.context, "order is Accepted", Toast.LENGTH_SHORT).show()
                        itemClicked.OnItemAcceptListener(position)
                    } else {// remove if it is dispatched
                        customernames.removeAt(position)
                        quantity.removeAt(position)
                        images.removeAt(position)
                        notifyItemRemoved(adapterPosition)
                        Toast.makeText(it.context, "order is Dispatched", Toast.LENGTH_SHORT).show()
                        itemClicked.OnItemDispatchListener(position)

                    }
                }
            }
            itemView.setOnClickListener { /// click on the item to show the details
                itemClicked.OnItemClickListener(position)
            }

        }
    }


}