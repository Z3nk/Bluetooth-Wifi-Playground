package com.example.blapoc

import android.net.wifi.ScanResult
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class WifiAdapter(private var data: List<ScanResult>,
                  val onClick: (video: ScanResult) -> Unit
) : RecyclerView.Adapter<WifiViewHolder>() {


    override fun onBindViewHolder(holder: WifiViewHolder, position: Int) {
        val scans = data[position]
        holder.tvName?.text = data[position].SSID
        holder.container?.setOnClickListener {
            onClick(scans)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiViewHolder {
        return WifiViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cell_wifi,
                parent,
                false
            )
        )
    }

    fun refreshListWith(list: List<ScanResult>) {
        data = list
        notifyDataSetChanged()
    }

}