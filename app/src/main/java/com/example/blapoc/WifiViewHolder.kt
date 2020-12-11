package com.example.blapoc

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WifiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val container: View? = itemView.findViewById(R.id.container)
    val tvName: TextView? = itemView.findViewById(R.id.tv_name)
}