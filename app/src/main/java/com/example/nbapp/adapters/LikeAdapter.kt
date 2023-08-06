package com.example.nbapp.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nbapp.databinding.NotebookMainItemBinding
import com.example.nbapp.models.LikeModel

class LikeAdapter(
    private val context: Context,
    private val list: ArrayList<LikeModel>,
    private val productClickInterface: LikedProductOnClickInterface,
    private val likeClickInterface: LikedOnClickInterface,
):RecyclerView.Adapter<LikeAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: NotebookMainItemBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NotebookMainItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.tvNameNBDisplayItem.text = "${currentItem.brand} ${currentItem.name}"
        holder.binding.tvPriceNBDisplayItem.text = "${currentItem.price}"
        holder.binding.btnLike.backgroundTintList = ColorStateList.valueOf(Color.RED)


        Glide
            .with(context)
            .load(currentItem.imageUrl)
            .into(holder.binding.ivNBDisplayItem)


        holder.itemView.setOnClickListener {
            productClickInterface.onClickProduct(list[position])
        }

        holder.binding.btnLike.setOnClickListener {
            likeClickInterface.onClickLike(currentItem)
            holder.binding.btnLike.backgroundTintList = ColorStateList.valueOf(Color.WHITE)

            likeClickInterface.onClickLike(currentItem)
        }
    }
}

interface LikedProductOnClickInterface{
    fun onClickProduct (item: LikeModel)
}

interface LikedOnClickInterface{
    fun onClickLike (item: LikeModel)
}