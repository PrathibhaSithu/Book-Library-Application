package com.example.book_library.models

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.book_library.R

class BookView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_view)
    }
}