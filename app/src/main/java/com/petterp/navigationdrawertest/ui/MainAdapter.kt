package com.petterp.navigationdrawertest.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.petterp.navigationdrawertest.R

/**
 *
 * @author petterp To 2022/5/28
 */
class MainAdapter(val data: List<String>) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv: TextView

        init {
            tv = itemView.findViewById(R.id.tvItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.tv.text = data[position]
    }

    override fun onViewRecycled(holder: MainViewHolder) {
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int = data.size
}
