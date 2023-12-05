package com.example.book_library.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.book_library.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import android.content.Intent
import android.widget.Toast


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.linktosignin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signUp.setOnClickListener {
            val email = binding.stdEmail.text.toString()
            val pass = binding.stdpass.text.toString()
            val confirmPass = binding.edtConfirmPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT)
                        }
                    }
                } else {
                    Toast.makeText(this, "Empty Fields are not allowed", Toast.LENGTH_SHORT)
                }
            }


        }
    }
}