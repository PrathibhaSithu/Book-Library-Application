package com.example.quize.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.quize.R
import com.example.quize.models.ExamModel
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class addQuizepart2 : AppCompatActivity() {


    private lateinit var tvExamID :TextView
    private lateinit var tvExamName :TextView
    private lateinit var tvExamDate :TextView
    private lateinit var tvExamType :TextView
    private lateinit var tvGarde :TextView
    private lateinit var tvNoOfQuestion :TextView
    private lateinit var tvSubjectName :TextView
    private lateinit var btnUpdate :Button
    private lateinit var ButtonDelete :Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewquize)
        initView()
        setValuesToViews()

        val button4 = findViewById<Button>(R.id.button4)
        button4.setOnClickListener{
            openUpdateDialog(
                intent.getStringExtra("ExamID").toString(),
                intent.getStringExtra("ExamName").toString()
            )

        }


        val button5 = findViewById<Button>(R.id.button5)
        button5.setOnClickListener{
            deleteRecode(
                intent.getStringExtra("ExamID").toString()
            )

        }


    }

    private fun deleteRecode(ExamID: String) {
        val dbRef =FirebaseDatabase.getInstance().getReference("Exam").child(ExamID)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this,"Exam data Deleted ",Toast.LENGTH_LONG).show()
            val intent = Intent(this, FechingExam::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{
            error -> Toast.makeText(applicationContext,"Exam data Deleted Error ${error.message} ",Toast.LENGTH_LONG).show()

        }
    }

    private fun initView(){
        tvExamID = findViewById(R.id.tvExamID) // initialize tvExamID
        tvExamName = findViewById(R.id.ExamName) // initialize tvExamID
        tvExamDate = findViewById(R.id.ExamDate) // initialize tvExamID
        tvExamType = findViewById(R.id.ExamType) // initialize tvExamID
        tvGarde = findViewById(R.id.Garde) // initialize tvExamID
        tvNoOfQuestion = findViewById(R.id.NoOfQuestion) // initialize tvExamID
        tvSubjectName = findViewById(R.id.SubjectName) // initialize tvExamID

    }

    private fun setValuesToViews(){

        tvExamID.text = intent.getStringExtra("ExamID")
        tvExamName.text = intent.getStringExtra("ExamName")
        tvExamDate.text = intent.getStringExtra("ExamDate")
        tvExamType.text = intent.getStringExtra("ExamType")
        tvGarde.text = intent.getStringExtra("Garde")
        tvNoOfQuestion.text = intent.getStringExtra("NoOfQuestion")
        tvSubjectName.text = intent.getStringExtra("SubjectName")
    }

    private fun openUpdateDialog(
        ExamID:String,
        ExamName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.activity_edit_quiz_part1,null)

        mDialog.setView(mDialogView)

        val etExamName = mDialogView.findViewById<EditText>(R.id.editTextExamName)
        val etSubjectName = mDialogView.findViewById<EditText>(R.id.editTextSubjectName)
        val etGrade = mDialogView.findViewById<EditText>(R.id.editTextGrade)
        val etExamType = mDialogView.findViewById<EditText>(R.id.editTextExamType)
        val etDate = mDialogView.findViewById<EditText>(R.id.editTextDate)
        val etNoOfQuestion = mDialogView.findViewById<EditText>(R.id.editTextNoOfQuestion)



        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdate)

        etExamName.setText(intent.getStringExtra("ExamName").toString())
        etSubjectName.setText(intent.getStringExtra("SubjectName").toString())
        etGrade.setText(intent.getStringExtra("Garde").toString())
        etExamType.setText(intent.getStringExtra("ExamType").toString())
        etDate.setText(intent.getStringExtra("ExamDate").toString())
        etNoOfQuestion.setText(intent.getStringExtra("NoOfQuestion").toString())

        mDialog.setTitle("Updating $ExamName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        // Create a Calendar instance to store the selected date
        val calendar = Calendar.getInstance()

        // Set a listener for the date EditText field to open the date picker dialog
        etDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create a date picker dialog and set its listener to update the date EditText field
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val formattedDate = "${dayOfMonth}/${month + 1}/${year}"
                    etDate.setText(formattedDate)
                },
                year,
                month,
                day
            )

            datePicker.show()
        }

        btnUpdateData.setOnClickListener{
            val examName = etExamName.text.toString().trim()
            val subjectName = etSubjectName.text.toString().trim()
            val grade = etGrade.text.toString().trim()
            val examType = etExamType.text.toString().trim()
            val date = etDate.text.toString().trim()
            val noOfQuestion = etNoOfQuestion.text.toString().trim()

            if (examName.isEmpty()) {
                etExamName.error = "Exam name is required"
                return@setOnClickListener
            }

            if (subjectName.isEmpty()) {
                etSubjectName.error = "Subject name is required"
                return@setOnClickListener
            }

            if (grade.isEmpty()) {
                etGrade.error = "Grade is required"
                return@setOnClickListener
            }

            if (examType.isEmpty()) {
                etExamType.error = "Exam type is required"
                return@setOnClickListener
            }

            if (date.isEmpty()) {
                etDate.error = "Date is required"
                return@setOnClickListener
            }

            if (noOfQuestion.isEmpty()) {
                etNoOfQuestion.error = "Number of questions is required"
                return@setOnClickListener
            }

            updateExamData(
                ExamID,
                examName,
                subjectName,
                grade,
                examType,
                date,
                noOfQuestion,
            )

            Toast.makeText(applicationContext,"Exam data Updated ",Toast.LENGTH_LONG).show()

            tvExamDate.text = date
            tvExamType.text = examType
            tvGarde.text = grade
            tvNoOfQuestion.text = noOfQuestion
            tvSubjectName.text = subjectName
            tvExamName.text = examName

            alertDialog.dismiss()
        }
    }

    private fun updateExamData(
        ExamID: String,
        ExamName: String,
        ExamType: String,
        Garde: String,
        NoOfQuestion: String,
        SubjectName: String,
        ExamDate:String


    ){
        val dbRef =FirebaseDatabase.getInstance().getReference("Exam").child(ExamID)
        val examInfo = ExamModel(ExamName,ExamType,Garde,NoOfQuestion,SubjectName,ExamDate)
        dbRef.setValue(examInfo)
    }
}