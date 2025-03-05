package com.example.food_appadmin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.disklrucache.DiskLruCache
import com.example.food_appadmin.adapter.deliveryAdapter
import com.example.food_appadmin.databinding.ActivityOutforDeliveryBinding
import com.example.food_appadmin.dataentity.orderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class outforDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutforDeliveryBinding by lazy {
        ActivityOutforDeliveryBinding.inflate(
            layoutInflater
        )
    }

    private lateinit var database: FirebaseDatabase
    private var listofCompleteOrder: ArrayList<orderDetails> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // retrieve and display the completed order
        retrieveAndDisplayCompletedOrder()

        // back btn
        binding.backbtn.setOnClickListener { finish() }

    }

    private fun retrieveAndDisplayCompletedOrder() {
        // initialize firebase database
        database = FirebaseDatabase.getInstance()
        val databaseRef = database.reference.child("CompleteOrder").orderByChild("CurrentItem")

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // clear the list before giving the new data
                listofCompleteOrder.clear()
                for (ordersnapshot in snapshot.children) {
                    val completeOrder = ordersnapshot.getValue(orderDetails::class.java)
                    completeOrder.let { listofCompleteOrder.add(it!!) }
                }
                // reverse the order list to display the first
                listofCompleteOrder.reversed()
                setAdapter()
            }

            private fun setAdapter() {
                // list to hold the customer name and payment status
                val customerName = mutableListOf<String>()
                val paymentStatus = mutableListOf<Boolean>()
                for (order in listofCompleteOrder) {
                    order.userName.let { customerName.add(it!!) }
                    order.paymentReceived.let { paymentStatus.add(it) }
                }
                val adapter = deliveryAdapter(customerName, paymentStatus )
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}