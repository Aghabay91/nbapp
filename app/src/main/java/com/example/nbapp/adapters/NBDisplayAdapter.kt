package com.example.nbapp.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nbapp.databinding.NotebookMainItemBinding
import com.example.nbapp.models.NBDisplayModel

class NBDisplayAdapter(
    private val context: Context,
    private var list: List<NBDisplayModel>,
    private val productClickInterface: ProductOnClickInterface,
    private val likeClickInterface: LikeOnClickInterface,
):RecyclerView.Adapter<NBDisplayAdapter.ViewHolder>() {

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


            Glide
                .with(context)
                .load(currentItem.imageUrl)
                .into(holder.binding.ivNBDisplayItem)


            holder.itemView.setOnClickListener {
                productClickInterface.onClickProduct(list[position])
            }

            holder.binding.btnLike.setOnClickListener {
                if(holder.binding.btnLike.isChecked){
                    holder.binding.btnLike.backgroundTintList = ColorStateList.valueOf(Color.RED)
                    likeClickInterface.onClickLike(currentItem)
                }
                else{
                    holder.binding.btnLike.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                }

            }

        }
    fun update(item:List<NBDisplayModel>){
        this.list=item
        notifyDataSetChanged()
    }
}



interface ProductOnClickInterface{
    fun onClickProduct(item: NBDisplayModel)
}

interface LikeOnClickInterface{
    fun onClickLike(item: NBDisplayModel)
}