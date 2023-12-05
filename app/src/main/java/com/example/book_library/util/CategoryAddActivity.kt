package com.example.book_library.util

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.book_library.R
import com.example.book_library.databinding.ActivityCategoryAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CategoryAddActivity : AppCompatActivity() {

    //view binding , firebaseAuth , progress Dialog
    private lateinit var binding:ActivityCategoryAddBinding
    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init  //firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait ...")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click go back
        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

        //handle click, begin upload cats
        binding.submitBtn.setOnClickListener {
            validateData()
        }
    }
    private var category = ""
    private fun validateData(){
        //validate data

        //get data
        category = binding.categoryEt.text.toString().trim()

        //validate data
        if (category.isEmpty()){
            Toast.makeText(this,"Enter Category ...", Toast.LENGTH_SHORT).show()
        }
        else{
            addCategoryFirebase()
        }
    }

    private fun addCategoryFirebase() {
        //show progress
        progressDialog.show()

        //get timestamp
        val timestamp = System.currentTimeMillis()

        //setup data to add in firebase db
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["Category"] = category
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = "${firebaseAuth.uid}"

        //add to firebase db: Database Root > CatId > Cat info
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Data Added Successfully ..", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to add due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }
}