package com.example.book_library.util

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.book_library.R

class  EditBookActivity : AppCompatActivity() {

    private lateinit var book_id: TextView
    private lateinit var book_category: TextView
    private lateinit var book_name: TextView
    private lateinit var book_img: Image
    private lateinit var edit_book: Button
    private lateinit var delete_book: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)

        book_id.text=intent.getStringExtra("bookId")
        book_category.text = intent.getStringExtra("bookCategory")
        book_name.text = intent.getStringExtra("bookName")
    }
}