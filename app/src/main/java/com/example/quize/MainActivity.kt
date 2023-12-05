package com.example.quize

import android.content.Intent
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.quize.activities.FechingExam
import com.example.quize.activities.addQuizPart1

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val firebase :DatabaseReference =FirebaseDatabase.getInstance().getReference()




            val button8 = findViewById<Button>(R.id.button8)
            button8.setOnClickListener{
            val intent = Intent(this, FechingExam::class.java)
            startActivity(intent)
          }

    }

}