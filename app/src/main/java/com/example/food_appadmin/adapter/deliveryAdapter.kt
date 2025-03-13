package com.example.food_appadmin.adapter

import android.R
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.food_appadmin.databinding.ItemForDeliveryrvBinding


class deliveryAdapter(
    private val customerNames: MutableList<String>,
    private val paymentStatus: MutableList<Boolean>, // received or not received

) : RecyclerView.Adapter<deliveryAdapter.deliveryViewholder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): deliveryViewholder {
        val binding =
            ItemForDeliveryrvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return deliveryViewholder(binding)
    }

    override fun onBindViewHolder(
        holder: deliveryViewholder,
        position: Int,
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return customerNames.size
    }

    inner class deliveryViewholder(private val binding: ItemForDeliveryrvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                if (paymentStatus[position] == true) {
                    payment.text = "Received"
                } else {
                    payment.text = "notReceived"
                }


                // now for the color
                val colorMap = mapOf(
                  true to Color.GREEN,
                  false to Color.RED,
                   
                )

                payment.setTextColor(
                    colorMap[paymentStatus[position]] ?: Color.BLACK
                ) // default to black if not found
                statusColor.setCardBackgroundColor(colorMap[paymentStatus[position]] ?: Color.BLACK)

            }
        }


    }
}