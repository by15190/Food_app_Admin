package com.example.food_appadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.InspectableProperty
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_appadmin.adapter.pendingorderAdapter
import com.example.food_appadmin.databinding.ActivityPendingDeliveryBinding
import com.example.food_appadmin.dataentity.orderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class pendingDeliveryActivity : AppCompatActivity(), pendingorderAdapter.OnItemClicked {
    private val binding: ActivityPendingDeliveryBinding by lazy {
        ActivityPendingDeliveryBinding.inflate(
            layoutInflater
        )
    }
    private var listofName: MutableList<String> = mutableListOf()
    private var listofTotalPrice: MutableList<String> = mutableListOf()
    private var listofimageFirstFoodOrder: MutableList<String> = mutableListOf()
    private var listofOrderItem: MutableList<orderDetails> =
        mutableListOf() /// list of items in the order
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        // initialize the database reference
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference.child("orders")

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getOrderDetails()

        // back btn
        binding.backbtn.setOnClickListener {
            finish()
        }

    }

    private fun getOrderDetails() {
        // retrieve the order details from the database
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnap in snapshot.children) {
                    val order = datasnap.getValue(orderDetails::class.java)
                    order.let { listofOrderItem.add(it!!) }
                }
                addDataToListforRV()

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addDataToListforRV() {
        /// add the data to the list
        for (orderItems in listofOrderItem) {
            orderItems.userName.let { listofName.add(it!!) }
            orderItems.totalPrice.let { listofTotalPrice.add(it!!) }
            orderItems.foodImages.filterNot { it.isEmpty() }
                .forEach { listofimageFirstFoodOrder.add(it) }
        }
        setAdapter()
    }

    private fun setAdapter() {
        val adapter =
            pendingorderAdapter(this, listofName, listofTotalPrice, listofimageFirstFoodOrder, this)
        binding.pendingOrderRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.pendingOrderRecyclerview.adapter = adapter
    }


    override fun OnItemClickListener(position: Int) {
        val intent = Intent(this, orderDetailsActivity::class.java)
        val orderDetails = listofOrderItem[position]
        intent.putExtra("orderDetails", orderDetails)
        startActivity(intent)

    }

    override fun OnItemAcceptListener(position: Int) {
        // handle item Acceptance and update the database
        val childItemPushKey = listofOrderItem[position].itemPushKey
        val clickitemorderRef = childItemPushKey.let {
            database.reference.child("orderDetails").child(it!!)
        }
        clickitemorderRef.child("orderAccepted").setValue(true)
        updateOrderAcceptStatus(position)
    }

    private fun updateOrderAcceptStatus(position: Int) {
        // update the order acceptance in the user's buyHistory and orderDetails
        val clickitemUserID = listofOrderItem[position].userUID
        val pushkeyOfclickedItem = listofOrderItem[position].itemPushKey
        val buyHistroyref =
            database.reference.child("users").child(clickitemUserID.toString()).child("BuyHistory")
                .child(pushkeyOfclickedItem!!)
        buyHistroyref.child("OrderAccepted").setValue(true)

        databaseReference.child(pushkeyOfclickedItem).child("OrderAccepted").setValue(true)
    }

    override fun OnItemDispatchListener(position: Int) {
        // handle item Dispatch and update the database
        val itemDispatchPushkey = listofOrderItem[position].userUID
        val dispatchref = database.reference.child("CompletedOrders").child(itemDispatchPushkey!!)
        dispatchref.setValue(listofOrderItem[position]).addOnSuccessListener {
            deletethisItemfromPendingOrderlist(itemDispatchPushkey)
        }

    }

    private fun deletethisItemfromPendingOrderlist(dispatchpushKey: String) {
        val pendingOrderref = database.reference.child("orderDetails").child(dispatchpushKey)
        pendingOrderref.removeValue().addOnSuccessListener {
            Toast.makeText(this, "Order Dispatched", Toast.LENGTH_SHORT).show()
        }
    }

}