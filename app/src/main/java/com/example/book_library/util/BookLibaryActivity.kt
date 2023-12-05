package com.example.book_library.util

import android.content.AbstractThreadedSyncAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Adapter
import android.widget.Button
import com.example.book_library.R
import com.example.book_library.adapters.AdapterCategory
import com.example.book_library.databinding.ActivityBookLibaryBinding
import com.example.book_library.fragments.BookHomeFragment
import com.example.book_library.fragments.BookListFragment
import com.example.book_library.models.ModelCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class BookLibaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookLibaryBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var categoryArrayList: ArrayList<ModelCategory>
    private lateinit var adapterCategory: AdapterCategory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookLibaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        loadCategories()

        binding.searchEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapterCategory.filter.filter(s)
                }
                catch (e: Exception){

                }
            }
            override fun afterTextChanged(p0: Editable?) {

            }
        })

        if(firebaseAuth.currentUser != null){
            firebaseAuth.currentUser?.let {
                binding.EmailUser.text = it.email
            }
        }
        //logout
        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }
        binding.btnAddCat.setOnClickListener {
            startActivity(Intent(this, CategoryAddActivity :: class.java))
        }

        //handle click, start add pdf page
        binding.addPdfFab.setOnClickListener {
            startActivity(Intent(this,PdfAddActivity::class.java))
        }
    }

    private fun loadCategories() {
        // init arrayList
        categoryArrayList = ArrayList()

        //get data from fDB
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for(ds in snapshot.children){
                    var model = ds.getValue(ModelCategory::class.java)
                    categoryArrayList.add(model!!)
                }
                adapterCategory =  AdapterCategory(this@BookLibaryActivity, categoryArrayList)

                binding.categoriesRv.adapter = adapterCategory
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}