package com.example.quize.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quize.R
import com.example.quize.models.ExamModel


class ExamAdapter (private val examList: ArrayList<ExamModel>) :
    RecyclerView.Adapter<ExamAdapter.ViewHolder>() {

    private lateinit var mListener:onItemClikListener

    interface onItemClikListener{
        fun onItemClik(position: Int)
    }

    fun setOnItemClikListener(clikListener: onItemClikListener){
        mListener=clikListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.card_view_exam,parent,false)
        return ViewHolder(itemView,mListener)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val currentExam = examList[position]
        holder.tvExamName.text=currentExam.ExamName
        holder.tvSubject.text=currentExam.SubjectName
        holder.tvExamType.text=currentExam.ExamType
        holder.tvNoOfQuestion.text=currentExam.NoOfQuestion
        holder.tvExamDate.text=currentExam.ExamDate
    }


    override fun getItemCount(): Int {
        return examList.size
    }



    class ViewHolder(itemView:View,clikListener: onItemClikListener):RecyclerView.ViewHolder(itemView){
        val  tvExamName:TextView=itemView.findViewById(R.id.tvExamName)
        val  tvSubject:TextView=itemView.findViewById(R.id.tvSubject)
        val  tvExamType:TextView=itemView.findViewById(R.id.tvExamType)
        val  tvNoOfQuestion:TextView=itemView.findViewById(R.id.tvNoOfQuestion)
        val  tvExamDate:TextView=itemView.findViewById(R.id.tvExamDate)

        init {
            itemView.setOnClickListener{
                clikListener.onItemClik(adapterPosition)
            }
        }
    }


}