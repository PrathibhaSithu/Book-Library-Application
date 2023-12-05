package com.example.book_library.util

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.book_library.R
import com.example.book_library.databinding.ActivityPdfAddBinding
import com.example.book_library.models.ModelCategory
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class PdfAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfAddBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var categoryArrayList: ArrayList<ModelCategory>

    //uri of picked pdf
    private var pdfUri: Uri? = null
    //TAG
    private val TAG = "PDF_ADD_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        loadPdfCategories()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.backBtn.setOnClickListener{
            onBackPressed()
        }
        binding.categoryTv.setOnClickListener {
            categoryPickDialog()
        }
        binding.attachPdfBtn.setOnClickListener{
            pdfPickIntent()
        }
        binding.submitBtn.setOnClickListener {
            validateData()
        }
    }

    private var title = ""
    private var description = ""
    private var Category = ""

    private fun validateData() {
        Log.d(TAG, "validateData: validating data")

        title = binding.titleEt.text.toString().trim()
        description = binding.descriptionEt.text.toString().trim()
        Category = binding.categoryTv.text.toString().trim()

        if(title.isEmpty()){
            Toast.makeText(this,"Enter Title....", Toast.LENGTH_SHORT).show()
        }
        else if(description.isEmpty()){
            Toast.makeText(this,"Enter Description....", Toast.LENGTH_SHORT).show()
        }
        else if(Category.isEmpty()){
            Toast.makeText(this,"Pick A Category....", Toast.LENGTH_SHORT).show()
        }
        else if(pdfUri == null){
            Toast.makeText(this,"Pick PDF....", Toast.LENGTH_SHORT).show()

        }
        else{
            uploadPdfToStorage()
        }
    }

    private fun uploadPdfToStorage() {
        Log.d(TAG, "UploadPdfToStorage : uploadingto storage...")

        progressDialog.setMessage("Uploading PDF")
        progressDialog.show()

        val timestamp = System.currentTimeMillis()

        val filePathAndName = "Books/$timestamp"

        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(pdfUri!!)
            .addOnSuccessListener {taskSnapshot ->
                Log.d(TAG, "uploadPdfToStorage: PDF uploaded now getting url...")

                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadPdfUrl = "${uriTask.result}"
                
                uploadPdfInfoToDb(uploadPdfUrl,timestamp)
            }
            .addOnFailureListener{e->
                Log.d(TAG, "UploadPdfToStorage : failed to upload due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to upload due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadPdfInfoToDb(uploadPdfUrl: String, timestamp: Long) {
        Log.d(TAG, "UplaodPdfInfoToDb: uploading to db")
        progressDialog.setMessage("Uploading pdf info...")

        val uid = firebaseAuth.uid

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["uid"] = "$uid"
        hashMap["id"] = "$timestamp"
        hashMap["title"] = "$title"
        hashMap["description"] = "$description"
        hashMap["categoryId"] = "$selectedCategoryId"
        hashMap["url"] = "$uploadPdfUrl"
        hashMap["timestamp"] = timestamp
        hashMap["viewCount"] = 0
        hashMap["downloadCount"] = 0

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "uploadPdfInfoToDb : upload to db")
                progressDialog.dismiss()
                Toast.makeText(this,"Upload...", Toast.LENGTH_SHORT).show()
                pdfUri = null
            }
            .addOnFailureListener { e->
                Log.d(TAG, "uploadPdfInfoToDb : failed to upload due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to upload due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadPdfCategories() {
        Log.d(TAG,"loadPdfCategories : Loading pdf categories")
        //init arrayList
        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for(ds in snapshot.children){
                    val model = ds.getValue(ModelCategory::class.java)
                    categoryArrayList.add(model!!)
                    Log.d(TAG, "onDataChange : ${model.Category}")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    private var selectedCategoryId = ""
    private var selectCategoryTitle = ""

    private fun categoryPickDialog(){
        Log.d(TAG, "categoryPickDialog : Showing pdf category pick dialog")

        val categoriesArray = arrayOfNulls<String>(categoryArrayList.size)
        for(i in categoryArrayList.indices){
            categoriesArray[i] = categoryArrayList[i].Category
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Category")
            .setItems(categoriesArray){dialog, which ->
                selectCategoryTitle = categoryArrayList[which].Category
                selectedCategoryId = categoryArrayList[which].id

                binding.categoryTv.text = selectCategoryTitle

                Log.d(TAG, "categoryPickDialog : Selected Category ID : $selectedCategoryId")
                Log.d(TAG, "categoryPickDialog : Selected Category Title : $selectCategoryTitle")
            }
            .show()
    }
    private fun pdfPickIntent(){
        Log.d(TAG, "pdfPickIntent : starting pdf intent")

        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        pdfActivityResultLauncher.launch(intent)
    }
    val pdfActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>{ result ->
            if(result.resultCode == RESULT_OK){
                Log.d(TAG, "PDF Picked")
                pdfUri = result.data!!.data
            }
            else{
                Log.d(TAG, "PDF Pick Cancelled")
                Toast.makeText(this,"Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )
}