package com.example.food_appadmin.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_appadmin.databinding.OrderdetailsiItemlistRvItemBinding



class orderItemsAdapter(
    private val context: Context,
    private val name: ArrayList<String>,
    private val price: ArrayList<String>,
    private val quantity: ArrayList<String>,
    private val images: ArrayList<String>,
) : RecyclerView.Adapter<orderItemsAdapter.viewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): viewHolder {
        val binding = OrderdetailsiItemlistRvItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return viewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: viewHolder,
        position: Int,
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return name.size
    }

    inner class viewHolder(private val binding: OrderdetailsiItemlistRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                itemname.text = name[position]
                itemQuantity.text = quantity[position]
                itemprice.text = price[position]

                val uri = Uri.parse(images[position])
                Glide.with(context).load(uri).into(itemimage)
            }
        }
    }

}