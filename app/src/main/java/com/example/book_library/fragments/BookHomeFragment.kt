package com.example.book_library.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import com.example.book_library.R
import com.example.book_library.databinding.FragmentBookHomeBinding
import com.google.firebase.database.DatabaseReference
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineStart
import java.io.ByteArrayOutputStream
import java.lang.Exception
//import kotlin.io.encoding.Base64
import android.util.Base64
import com.example.book_library.models.BookItemDC
import com.google.firebase.database.FirebaseDatabase

class BookHomeFragment : Fragment() {
    private lateinit var binding: FragmentBookHomeBinding
    private lateinit var db: DatabaseReference

    private lateinit var myActivityResultLauncher: ActivityResultLauncher<Intent>

    var nodeId = ""
    var sImage:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookHomeBinding.inflate(inflater,container,false)
        val root: View = binding.root

        binding.btnAddimg.setOnClickListener(){
            var myfileintent = Intent(Intent.ACTION_GET_CONTENT)
            myfileintent.setType("image/*")
            ActivityResultLauncher.launch(myfileintent)
        }
        binding.btnAdd.setOnClickListener() {
            add_Data()
        }

        return  root
    }

    private fun add_Data(){
        val itemName = binding.etId.text.toString()
        val itemId = binding.etName.text.toString()
        val itemCategory = binding.etCat.text.toString()

        db = FirebaseDatabase.getInstance().getReference("items")
        val item = BookItemDC(itemName,itemId,itemCategory,sImage)
        val databaseReference = FirebaseDatabase.getInstance().reference
        val id = databaseReference.push().key
        db.child(id.toString()).setValue(item).addOnSuccessListener {
            binding.etName.text.clear()
            binding.etId.text.clear()
            binding.etCat.text.clear()
            sImage = ""
            binding.imageView2.setImageBitmap(null)

            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(context, "Data Inserted", Toast.LENGTH_SHORT).show()
        }
    }

    private val ActivityResultLauncher = registerForActivityResult<Intent,ActivityResult>(
            ActivityResultContracts.StartActivityForResult())
    {result: ActivityResult ->
        if(result.resultCode == AppCompatActivity.RESULT_OK){
            val uri = result.data!!.data
            try{
                val inputStream = context?.contentResolver?.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStream)
                val stream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
                val bytes = stream.toByteArray()
                sImage = Base64.encodeToString(bytes,Base64.DEFAULT)
                binding.imageView2.setImageBitmap(myBitmap)
                inputStream!!.close()
                Toast.makeText(context,"Image Selected",Toast.LENGTH_SHORT).show()
            }catch (ex: Exception){
                Toast.makeText(context, ex.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}