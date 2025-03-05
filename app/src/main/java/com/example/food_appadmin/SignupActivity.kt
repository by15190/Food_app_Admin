package com.example.food_appadmin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food_appadmin.databinding.ActivitySignupBinding
import com.example.food_appadmin.dataentity.userModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var restorentName: String
    private lateinit var database: DatabaseReference

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        /// initialize auth
        auth = FirebaseAuth.getInstance()


        // initialize database
        database = Firebase.database.reference


        /// list of locations
        val locationlist = arrayOf("Jaipur", "Delhi", "Mumbai", "Bangalore", "Kolkata")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationlist)
        // Set the adapter to the AutoCompleteTextView
        binding.listofLocation.setAdapter(adapter)

        // already have account button
        binding.haveaccbtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        /// on create acc btn
        binding.createAccbtn.setOnClickListener {
            email = binding.signupemail.text.toString().trim()
            password = binding.signuppassword.text.toString().trim()
            userName = binding.name.text.toString().trim()
            restorentName = binding.restorentname.text.toString().trim()

            if (email.isBlank() || password.isBlank() || userName.isBlank() || restorentName.isBlank()) {
                Toast.makeText(this, "fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                createaccount(email, password)
            }
        }
    }

    private fun createaccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "account created successfully ", Toast.LENGTH_SHORT).show()
                saveUSerdata()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(
                    this,
                    "account creation failed:${task.exception?.message.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // save data in database
    private fun saveUSerdata() {
        email = binding.signupemail.text.toString().trim()
        password = binding.signuppassword.text.toString().trim()
        userName = binding.name.text.toString().trim()
        restorentName = binding.restorentname.text.toString().trim()

        val user = userModel(userName, restorentName, email, password)
        val userid = FirebaseAuth.getInstance().currentUser!!.uid // generate unique id
        Log.d("tag1", user.toString())

        // save data in database
        database.child("users").child(userid).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("tag1", "complete")
            } else {
                Log.d("tag1", task.exception?.message.toString())

            }
        }
    }
}