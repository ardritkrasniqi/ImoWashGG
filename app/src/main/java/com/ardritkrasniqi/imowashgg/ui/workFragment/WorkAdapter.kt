package com.ardritkrasniqi.imowashgg.ui.workFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ardritkrasniqi.imowashgg.R
import com.ardritkrasniqi.imowashgg.models.Records

class WorkAdapter(private val recordList: MutableList<Records>): RecyclerView.Adapter<WorkAdapter.RecordViewHolder>() {

    inner class RecordViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var time: TextView = itemView.findViewById(R.id.time_text)
        val product : TextView = itemView.findViewById(R.id.item_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_list,
                parent, false
            )
            return RecordViewHolder(itemView)
    }

    override fun getItemCount() = recordList.size

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val currentItem = recordList[position]
        if (currentItem.changed == true){
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        }
        holder.time.text = currentItem.time
        holder.product.text = currentItem.type
    }

    override fun getItemViewType(position: Int): Int {
        return if(recordList[position].changed == false){
            1
        } else 0


    }
}