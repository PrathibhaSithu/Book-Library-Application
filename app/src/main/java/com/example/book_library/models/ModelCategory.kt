package com.example.book_library.models

class ModelCategory {
    // variables
    var id:String = ""
    var Category:String = ""
    var timestamp:Long = 0
    var uid:String = ""

    //empty constructor
    constructor()

    //parameterized constructor
    constructor(id: String, Category: String, timestamp: Long, uid: String) {
        this.id = id
        this.Category = Category
        this.timestamp = timestamp
        this.uid = uid
    }
}