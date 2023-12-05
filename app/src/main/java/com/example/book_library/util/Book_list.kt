package com.example.book_library.util

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.book_library.R
import com.example.book_library.models.BookModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


private fun updateBookData(
        bookId:String,
        bookName:String,
        bookCategory:String,
        bookDescription:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("BookLibary").child(bookId)
        val bookinfo = BookModel(bookId,bookName,bookCategory,bookDescription)
        dbRef.setValue(bookinfo)
    }
