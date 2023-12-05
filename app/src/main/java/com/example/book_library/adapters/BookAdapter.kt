package com.example.book_library.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.book_library.R
import com.example.book_library.models.BookItemDC

class BookAdapter(private val itmlst: ArrayList<BookItemDC>) : RecyclerView.Adapter<BookAdapter.itmHolder>() {
    private lateinit var mListner: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListner = listener
    }

        class itmHolder(itmView: View, listener: OnItemClickListener):RecyclerView.ViewHolder(itmView){
            val itmname : TextView = itemView.findViewById(R.id.tv_name)
            val itmid : TextView = itemView.findViewById(R.id.tv_id)
            val itmcat : TextView = itemView.findViewById(R.id.tv_cat)
            val itmimg : ImageView = itemView.findViewById(R.id.bookImage)

            init {
                itemView.setOnClickListener{
                    listener.onItemClick(adapterPosition)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itmHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent,false)

        return itmHolder(itemView,mListner)
    }

    override fun onBindViewHolder(holder: itmHolder, position: Int) {
        val currentItem = itmlst[position]
        holder.itmname.text = currentItem.addBookName.toString()
        holder.itmid.text = currentItem.addBookId.toString()
        holder.itmcat.text = currentItem.addBookCategory.toString()

        //image
        val bytes = android.util.Base64.decode(currentItem.BookImg,android.util.Base64.DEFAULT)
        //convert image string format to bitmap image format
        val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        holder.itmimg.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return itmlst.size
    }
}