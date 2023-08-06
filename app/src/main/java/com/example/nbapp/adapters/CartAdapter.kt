package com.example.nbapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nbapp.SwipeToDelete
import com.example.nbapp.databinding.CartProductItemBinding
import com.example.nbapp.models.CartModel
import com.example.nbapp.models.NBDisplayModel

class CartAdapter(
    private val context: Context,
    private var list: ArrayList<CartModel>,
    private val onLongClickRemove: OnLongClickRemove
):RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    inner class ViewHolder(val binding:CartProductItemBinding):RecyclerView.ViewHolder(binding.root) {
        private val onSwipeDelete = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                list.removeAt(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CartProductItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        Glide
            .with(context)
            .load(currentItem.imageUrl)
            .into(holder.binding.ivCartProduct)


        holder.binding.tvCartProductName.text = currentItem.name
        holder.binding.tvCartProductPrice.text = "${currentItem.price}"
        holder.binding.tvCartItemCount.text = currentItem.quantity.toString()
        holder.binding.tvCartProductSize.text = currentItem.size

        var count = holder.binding.tvCartItemCount.text.toString().toInt()

        holder.binding.btnCartItemAdd.setOnClickListener {
//            count++
            // TODO: Update Quantity in Database also
//            holder.binding.tvCartItemCount.text = count.toString()

        }

        holder.binding.btnCartItemMinus.setOnClickListener {
//            count--
            // TODO: Update Quantity in Database also
//            holder.binding.tvCartItemCount.text = count.toString()
        }

        holder.itemView.setOnLongClickListener {
            onLongClickRemove.onLongRemove(currentItem , position)
            true
        }
    }
    interface OnLongClickRemove{
        fun onLongRemove(item: CartModel, position: Int)
    }
    fun update(item:List<CartModel>){
        this.list= item as ArrayList<CartModel>
        notifyDataSetChanged()
    }
}