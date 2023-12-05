package com.example.quize.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quize.R
import com.example.quize.models.ExamModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class addQuizPart1 : AppCompatActivity() {

    // declare your EditText fields
    private lateinit var etExamName:EditText
    private lateinit var etSubjectName:EditText
    private lateinit var etGarde:EditText
    private lateinit var etExamType:EditText
    private lateinit var etExamDate:EditText
    private lateinit var etNoOfQuestion:EditText

    private lateinit var  dbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quize_part1)

        // initialize your EditText fields
        etExamName = findViewById(R.id.editTextExamName)
        etSubjectName = findViewById(R.id.editTextSubjectName)
        etGarde = findViewById(R.id.editTextGrade)
        etExamType = findViewById(R.id.editTextExamType)
        etExamDate = findViewById(R.id.editTextDate)
        etNoOfQuestion = findViewById(R.id.editTextNoOfQuestion)

        // add an OnClickListener to the etExamDate EditText field
        etExamDate.setOnClickListener {
            // create a Calendar instance and set it to the current date
            val cal = Calendar.getInstance()

            // create a DatePickerDialog and set the current date as the default date
            val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                // set the selected date in the etExamDate EditText field
                etExamDate.setText("$dayOfMonth/${month + 1}/$year")
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

            // show the DatePickerDialog
            datePicker.show()
        }


        dbRef =FirebaseDatabase.getInstance().getReference("Exam")
        val btnNext = findViewById<Button>(R.id.btnUpdate)
        btnNext.setOnClickListener{
            // call your saveExamData function
            saveExamData()
        }

    }

    private fun saveExamData(){
        // get the text from each EditText field
        val ExamName = etExamName.text.toString()
        val SubjectName = etSubjectName.text.toString()
        val Garde = etGarde.text.toString()
        val ExamType = etExamType.text.toString()
        val ExamDate = etExamDate.text.toString()
        val NoOfQuestion = etNoOfQuestion.text.toString()

        // check if any of the EditText fields are empty
        if(ExamName.isEmpty()){
            etExamName.error="Please Enter Exam Name"
            return
        }
        if(SubjectName.isEmpty()){
            etSubjectName.error="Please Enter Subject Name"
            return
        }
        if(Garde.isEmpty()){
            etGarde.error="Please Enter Garde"
            return
        }
        if(ExamType.isEmpty()){
            etExamType.error="Please Enter Exam Type"
            return
        }
        if(ExamDate.isEmpty()){
            etExamDate.error="Please Enter Exam Date"
            return
        }
        if(NoOfQuestion.isEmpty()){
            etNoOfQuestion.error="Please Enter Number of Questions"
            return
        }

        // if all EditText fields are filled, save the data
        var ExamID =dbRef.push().key!!
        val Exam = ExamModel(ExamID, ExamName, SubjectName, Garde, ExamType, ExamDate, NoOfQuestion)
        dbRef.child(ExamID).setValue(Exam)
            .addOnCompleteListener{
                Toast.makeText(this,"Data Inserted Successfully",Toast.LENGTH_LONG).show()
                intent = Intent(this, FechingExam::class.java)
                startActivity(intent)
            }.addOnFailureListener{
                Toast.makeText(this,"Data Insertion Failed",Toast.LENGTH_LONG).show()
            }
    }

}
