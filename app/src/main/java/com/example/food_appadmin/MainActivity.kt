package com.example.food_appadmin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food_appadmin.adapter.pendingorderAdapter
import com.example.food_appadmin.databinding.ActivityMainBinding
import com.example.food_appadmin.dataentity.orderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var completeOrderReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Add item button
        binding.additembtn.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

        // All item button
        binding.allitemmenu.setOnClickListener {
            val intent = Intent(this, allitemmenuActivity::class.java)
            startActivity(intent)
        }

        binding.Logoutbtn.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // all deliveries
        binding.deliverys.setOnClickListener {
            val intent = Intent(this, outforDeliveryActivity::class.java)
            startActivity(intent)
        }

        // profile
        binding.profile.setOnClickListener {
            val intent = Intent(this, AdminprofileActivity::class.java)
            startActivity(intent)
        }

        // create new user
        binding.createnewuser.setOnClickListener {
            val intent = Intent(this, createUserActivity::class.java)
            startActivity(intent)

        }

        // pending order
        binding.pendingordertextview.setOnClickListener {
            val intent = Intent(this, pendingDeliveryActivity::class.java)
            startActivity(intent)
        }

        pendingOrders() // no of pending order
        completedOrders() // no of completed order
        totalRevenue() // total revenue
    }

    private fun totalRevenue() {
        completeOrderReference = database.reference.child("completedOrders")
        var listofTotalprice = mutableListOf<Int>()
        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnap in snapshot.children) {
                    val order = datasnap.getValue(orderDetails::class.java)
                    order?.totalPrice?.replace("$", "")?.toIntOrNull()
                        .let { listofTotalprice.add(it!!) }
                }

                // show the no of completed order
                binding.WholeEarning.text =
                    listofTotalprice.sum()
                        .toString() /// the count will not refresh until tne activity is restarted
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun completedOrders() {
        completeOrderReference = database.reference.child("completedOrders")
        var CompletedOrderCount = 0
        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                CompletedOrderCount =
                    snapshot.childrenCount.toInt() // get the no of completed order
                // show the no of completed order
                binding.completedOrderCount.text =
                    CompletedOrderCount.toString() /// the count will not refresh until tne activity is restarted
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun pendingOrders() {
        database = FirebaseDatabase.getInstance()
        completeOrderReference = database.reference.child("orderDetails")
        var pendingOrderCount = 0
        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderCount =
                    snapshot.childrenCount.toInt() // get the no of pending order
                // show the no of pending order
                binding.pendingorderCount.text =
                    pendingOrderCount.toString() /// the count will not refresh until tne activity is restarted
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}