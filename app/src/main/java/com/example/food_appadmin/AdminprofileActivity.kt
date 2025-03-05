package com.example.food_appadmin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food_appadmin.databinding.ActivityAdminprofileBinding
import com.example.food_appadmin.dataentity.userModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminprofileActivity : AppCompatActivity() {
    private val binding: ActivityAdminprofileBinding by lazy {
        ActivityAdminprofileBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var adminRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // initialize the auth and firebase
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        adminRef = database.reference.child("users")


        retreivedata()

        // back btn
        binding.backbtn.setOnClickListener {
            finish()
        }

        binding.apply {
            // all are disabled by default means they are visible to user but not editable
            profilename.isEnabled = false
            Address.isEnabled = false
            email.isEnabled = false
            phone.isEnabled = false
            password.isEnabled = false

            savebtn.isEnabled = false


        }
        var isEnabled = false

        /// edit btn
        binding.editbtn.setOnClickListener {
            isEnabled = !isEnabled

            binding.profilename.isEnabled = isEnabled
            binding.Address.isEnabled = isEnabled
            binding.email.isEnabled = isEnabled
            binding.phone.isEnabled = isEnabled
            binding.password.isEnabled = isEnabled

            binding.savebtn.isEnabled = isEnabled

            if (isEnabled) {
                binding.profilename.requestFocus()
            }
        }

        binding.savebtn.setOnClickListener {
            saveuserdataTodatabase()
        }

    }

    private fun saveuserdataTodatabase() {
        val name = binding.profilename.text.toString()
        val address = binding.Address.text.toString()
        val email = binding.email.text.toString()
        val phone = binding.phone.text.toString()
        val password = binding.password.text.toString()
        val adminuserid = auth.currentUser?.uid

        let {
            adminRef.child(adminuserid!!).child("name").setValue(name)
            adminRef.child(adminuserid).child("address").setValue(address)
            adminRef.child(adminuserid).child("email").setValue(email)
            adminRef.child(adminuserid).child("password").setValue(password)
            adminRef.child(adminuserid).child("phone").setValue(phone)
        }.addOnSuccessListener {
            Toast.makeText(this, "user details updated successfully", Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(email)
            auth.currentUser?.updatePassword(password)
        }.addOnFailureListener {
            Toast.makeText(this, "failed to update to user details", Toast.LENGTH_SHORT).show()

        }
    }

    private fun retreivedata() {
        val adminuserid = auth.currentUser?.uid
        adminRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var name = snapshot.child(adminuserid.toString()).child("name").getValue()
                    var phone = snapshot.child(adminuserid.toString()).child("phone").getValue()
                    var address = snapshot.child(adminuserid.toString()).child("address").getValue()
                    var email = snapshot.child(adminuserid.toString()).child("email").getValue()
                    var password =
                        snapshot.child(adminuserid.toString()).child("password").getValue()
                    setDataView(
                        name.toString(), phone.toString(),
                        address.toString(), email.toString(), password.toString()
                    )
                }
            }

            private fun setDataView(
                name: String?,
                phone: String?,
                address: String?,
                email: String?,
                password: String?,
            ) {
                binding.profilename.setText(name)
                binding.Address.setText(address)
                binding.email.setText(email)
                binding.phone.setText(phone)
                binding.password.setText(password)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}