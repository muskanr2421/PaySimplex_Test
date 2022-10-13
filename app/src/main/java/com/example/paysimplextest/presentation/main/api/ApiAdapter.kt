package com.example.paysimplextest.presentation.main.api

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.paysimplextest.R
import com.example.paysimplextest.domain.login.entity.Results

class ApiAdapter(val context: Context, private val myList: ArrayList<Results>): RecyclerView.Adapter<ApiAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = myList[position]
        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${position+1}.png"
        Glide.with(context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.image)
        holder.text.text = (currentItem.name).capitalize()
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.iv)
        val text : TextView = itemView.findViewById(R.id.tv)
    }

}