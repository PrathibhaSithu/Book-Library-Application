package com.example.book_library.util

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.book_library.R

class PdfListAdminActivity : AppCompatActivity() {

    private var categoryId = ""
    private var category = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_list_admin)

        val intent = intent
        categoryId = intent.getStringExtra("categoryId")!!
        category = intent.getStringExtra("category")!!

    }
}