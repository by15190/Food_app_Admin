package com.example.food_appadmin.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_appadmin.databinding.ItemForAlllitemmenuRvBinding
import com.example.food_appadmin.dataentity.foodItems
import com.google.firebase.database.DatabaseReference

class menuAdapter(
    private val context: Context,
    private val menuList: ArrayList<foodItems>,
    databaseReference: DatabaseReference,
    private val deleteItem: (position: Int) -> Unit,


    ) : RecyclerView.Adapter<menuAdapter.menuviewHolder>() {
    interface OnItemClick {
        fun onitemclcklistener(position: Int)
    }

    private val menuitemQuantities =
        MutableList<Int>(menuList.size) { 1 } // item quantity ///* initializes an IntArray with the same size as cart-items, setting each element to 1.
    //This ensures that every item in the cart starts with a default quantity of 1.


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): menuviewHolder {
        val binding: ItemForAlllitemmenuRvBinding =
            ItemForAlllitemmenuRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return menuviewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: menuviewHolder,
        position: Int,
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    inner class menuviewHolder(private val binding: ItemForAlllitemmenuRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val itemQuantity =
                    menuitemQuantities[position] // get the current quantity for the item at the given position

                val menuItem =
                    menuList[position] // get the MenuItem object for the item at the given position
                val uriString = menuItem.foodImage // get the URI string from the MenuItem object
                val uri =
                    Uri.parse(uriString)  // a method in Android development (Kotlin and Java) used to convert a string into a Uri object



                menuitemName.text = menuItem.foodname
                itemPrice.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(itemImage)
                itemquantity.text = itemQuantity.toString()

                // minus button
                minusimagebtn.setOnClickListener {
                    decrease(position)
                }


                // plus button
                plusimagebtn.setOnClickListener {
                    increace(position)

                }

                // delete button
                deletebtn.setOnClickListener {
                    val itemPosition =
                        adapterPosition // get the position of the item in the adapter
                    if (itemPosition != RecyclerView.NO_POSITION) { // check if the item position is valid
                        delete(position)
                    }

                }
            }


        }

        // fun to delete the item from the cart item list
        private fun delete(position: Int) {
            if (position >= 0 && position < menuList.size) {
                menuList.removeAt(position) // remove the item for the menu

                notifyItemRemoved(position) // notify the adapter that the item has been removed
                notifyItemRangeChanged(// notify the adapter that the item list has changed
                    position, menuList.size
                )
            }


        }


        // fun to decrease the quantity of the item
        private fun decrease(position: Int) {
            if (menuitemQuantities[position] > 1) {
                menuitemQuantities[position]--
                binding.itemquantity.text = menuitemQuantities[position].toString()
            }
        }

        // fun to increase the quantity of the item
        private fun increace(position: Int) {
            if (menuitemQuantities[position] < 10) {
                menuitemQuantities[position]++
                binding.itemquantity.text = menuitemQuantities[position].toString()
            }
        }


    }
}