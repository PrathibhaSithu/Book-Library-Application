package com.example.quize.activities
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quize.R
import com.example.quize.adapters.ExamAdapter
import com.example.quize.models.ExamModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class FechingExam : AppCompatActivity() {

    private lateinit var examRecucleView :RecyclerView
    private lateinit var  tvLoadingDat :TextView
    private lateinit var examList: ArrayList<ExamModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feching_exam_details)


        val button7 = findViewById<Button>(R.id.button7)
        button7.setOnClickListener{
            val intent = Intent(this, addQuizPart1::class.java)
            startActivity(intent)
        }


        examRecucleView=findViewById(R.id.rvexamget)
        examRecucleView.layoutManager=LinearLayoutManager(this)
        examRecucleView.setHasFixedSize(true)

        examList= arrayListOf<ExamModel>()

        val searchView = findViewById<SearchView>(R.id.SearchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filterExams(newText)
                }
                return true
            }
        })


        getExamData()

    }

    private fun filterExams(query: String) {
        val filteredList = ArrayList<ExamModel>()
        for (exam in examList) {
            if (exam.ExamName?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true) {
                filteredList.add(exam)
            }
        }
        val mAdapter = ExamAdapter(filteredList)
        examRecucleView.adapter = mAdapter
        mAdapter.setOnItemClikListener(object : ExamAdapter.onItemClikListener {
            override fun onItemClik(position: Int) {
                // Launch the details activity for the selected exam
            }
        })
    }


    private fun  getExamData(){
        val  dbRef = FirebaseDatabase.getInstance().getReference("Exam")
        dbRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                examList.clear()
                if(snapshot.exists()){
                    for (examSnap in snapshot.children){
                        val examData = examSnap.getValue(ExamModel::class.java)
                        examList.add(examData!!)
                    }
                    val mAdapter =ExamAdapter(examList)
                    examRecucleView.adapter=mAdapter

                    mAdapter.setOnItemClikListener(object :ExamAdapter.onItemClikListener{
                        override fun onItemClik(position: Int) {
                            val intent =Intent(this@FechingExam, addQuizepart2::class.java)
                            intent.putExtra("ExamID",examList[position].ExamID)
                            intent.putExtra("ExamName",examList[position].ExamName)
                            intent.putExtra("ExamDate",examList[position].ExamDate)
                            intent.putExtra("ExamType",examList[position].ExamType)
                            intent.putExtra("Garde",examList[position].Garde)
                            intent.putExtra("NoOfQuestion",examList[position].NoOfQuestion)
                            intent.putExtra("SubjectName",examList[position].SubjectName)


                            startActivity(intent)

                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }



        })

    }
}