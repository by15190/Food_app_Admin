package com.example.food_appadmin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_appadmin.adapter.menuAdapter
import com.example.food_appadmin.adapter.pendingorderAdapter
import com.example.food_appadmin.databinding.ActivityAllitemmenuBinding
import com.example.food_appadmin.dataentity.foodItems
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class allitemmenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllitemmenuBinding

    private lateinit var databaseref: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private val menuitems: ArrayList<foodItems> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        /// binding
        binding = ActivityAllitemmenuBinding.inflate(layoutInflater)

        /// firebase database ref
        databaseref = FirebaseDatabase.getInstance().reference

        retrieveMenuitems()



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // back btn
        binding.backbtn.setOnClickListener {
            finish()
        }

    }


    private fun retrieveMenuitems() {
        database = FirebaseDatabase.getInstance()
        val foodref: DatabaseReference = database.reference.child("menu")

        foodref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // clear existing data before adding new items
                menuitems.clear()

                // loop for thorough each item in the snapshot
                for (foodsnapshot in snapshot.children) {
                    val menuItem =
                        foodsnapshot.getValue(foodItems::class.java)// converts the snapshot data into an instance of the foodItems class.
                    menuItem?.let {
                        menuitems.add(it)
                    }

                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", error.message)
            }
        })

    }

    private fun setAdapter() {
        // adapter
        val adapter = menuAdapter(
            this@allitemmenuActivity,
            menuitems,
            databaseref
        ) { position ->
            deleteitem(position)

        }
        // recycler view
        binding.menuRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.menuRecyclerview.adapter = adapter


    }

    fun deleteitem(position: Int) {
        val menuitemToDelete = menuitems[position]
        val menuitemKey = menuitemToDelete.key
        val foodmenuref = database.reference.child("menu").child(menuitemKey.toString())

        foodmenuref.removeValue().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                menuitems.removeAt(position)
                binding.menuRecyclerview.adapter?.notifyItemRemoved(position) // Notify the adapter of the item removal
                Toast.makeText(this, "item delete successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "failed to delete the item", Toast.LENGTH_SHORT).show()
            }
        }
    }
}