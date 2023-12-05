package com.example.book_library.util

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.book_library.R
import com.example.book_library.databinding.FragmentBookHomeBinding
import com.example.book_library.models.BookModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.net.URI

class Add_new_book : AppCompatActivity() {

    private lateinit var binding:FragmentBookHomeBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    private lateinit var addBookId: EditText
    private lateinit var addBookName: EditText
    private lateinit var addBookCategory: EditText
    private lateinit var addBookDescription: EditText
    private lateinit var addBookPDF: Button
    private lateinit var bookAdd: Button
    private lateinit var backToHome:Button

    var  arrData = arrayOf("Science","Maths","English","History","Sinhala","Commerce")

    lateinit var ImageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBookHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val BookID = auth.currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("BookLibary")
        binding.btnAdd.setOnClickListener {
        }

        val arrAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,arrData)

        addBookId = findViewById(R.id.add_id_box)
        addBookName = findViewById(R.id.book_name_box)
        addBookCategory = findViewById(R.id.book_category_box)
        addBookDescription = findViewById(R.id.book_description_box)
        addBookPDF = findViewById(R.id.add_bookpdf_btn)
        bookAdd = findViewById(R.id.btnAdd)
        backToHome = findViewById(R.id.backtohome)

        binding.addBookPdf.setOnClickListener {
            selectImage()
        }
    }

    private fun saveBookData(){
        val bookId = addBookId.text.toString()
        val bookName = addBookName.text.toString()
        val bookCategory = addBookCategory.text.toString()
        val bookDescription = addBookDescription.text.toString()
        val bookPdf = addBookPDF.text.toString()

        if (bookId.isEmpty()){
            addBookId.error = "Please enter book Id"
        }
        if (bookName.isEmpty()){
            addBookName.error = "Please enter book name"
        }
        if (bookCategory.isEmpty()){
            addBookCategory.error = "Please enter book category"
        }
        if (bookDescription.isEmpty()){
            addBookDescription.error = "Please enter book description"
        }
        if (bookPdf.isEmpty()){
            addBookPDF.error = "Please enter book pdf"
        }

        dbRef.push().key!!

        val book = BookModel(bookId,bookName,bookCategory,bookDescription)

        dbRef.child(bookId).setValue(book)
            .addOnCompleteListener {
                Toast.makeText(this,"Data inserted successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener{ err ->
                Toast.makeText(this,"error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun selectImage(){
        val intent = Intent()
        intent.type = "images/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val imageUri = data?.data
            //binding.firebaseImage.setImageURI(imageUri)
        }
    }

}