package com.example.blapoc.wifi

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blapoc.R

class WifiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val container: View? = itemView.findViewById(R.id.container)
    val tvName: TextView? = itemView.findViewById(R.id.tv_name)
}