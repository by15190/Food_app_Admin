package com.example.food_appadmin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_appadmin.adapter.menuAdapter
import com.example.food_appadmin.adapter.orderItemsAdapter
import com.example.food_appadmin.databinding.ActivityOrderDetailsBinding
import com.example.food_appadmin.dataentity.orderDetails

class orderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailsBinding

    /// customer details
    private var customerName: String? = null
    private var customerAddress: String? = null
    private var customerPhone: String? = null
    private var customerTotalPrice: String? = null

    /// order details
    private lateinit var listofItemsName: ArrayList<String>
    private lateinit var listofItemsPrice: ArrayList<String>
    private lateinit var listofItemsQuantity: ArrayList<String>
    private lateinit var listofItemsImages: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.backbtn.setOnClickListener {
            finish()
        }

        getDataFromIntent()

    }

    private fun getDataFromIntent() {
        // get from the intent
        val intent = intent
        val orderDetails = intent.getParcelableExtra<orderDetails>("OrderDetails")

        if (orderDetails != null) {
            customerName = orderDetails.userName
            customerAddress = orderDetails.address
            customerPhone = orderDetails.phone
            customerTotalPrice = orderDetails.totalPrice

            listofItemsName = orderDetails.foodNames as ArrayList<String>
            listofItemsPrice = orderDetails.foodPrices as ArrayList<String>
            listofItemsImages = orderDetails.foodImages as ArrayList<String>
            listofItemsQuantity = orderDetails.foodQuantities as ArrayList<String>

            setCustomerDetails()
            setAdapter()
        }
    }

    private fun setAdapter() {
        val adapter = orderItemsAdapter(
            this,
            listofItemsName,
            listofItemsPrice,
            listofItemsQuantity,
            listofItemsImages
        )
        binding.orderItemsrv.layoutManager = LinearLayoutManager(this)
        binding.orderItemsrv.adapter = adapter

    }

    private fun setCustomerDetails() {
        binding.apply {
            name.text = customerName
            phone.text = customerPhone
            address.text = customerAddress
            totalPrice.text = customerTotalPrice
        }

    }
}