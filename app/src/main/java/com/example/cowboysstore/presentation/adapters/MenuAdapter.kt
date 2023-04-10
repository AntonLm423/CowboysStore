package com.example.cowboysstore.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cowboysstore.databinding.ItemMenuBinding

class MenuAdapter : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    var onItemClickListener: ((position: Int) -> Unit)? = null

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MenuItem>() {
        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder =
        MenuViewHolder(
            ItemMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        if(position == itemCount - 1) {
            holder.bindLastItem(differ.currentList[position])
        }
        else {
            holder.bind(differ.currentList[position])
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class MenuViewHolder(private val itemBinding: ItemMenuBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(menuItem: MenuItem) = with(itemBinding) {
            imageViewPicture.setImageResource(menuItem.iconResId)
            textViewItemName.text = menuItem.title

           itemMenu.setOnClickListener {
               onItemClickListener?.invoke(adapterPosition)
           }

        }

        fun bindLastItem(menuItem : MenuItem) = with(itemBinding) {
            imageViewPicture.setImageResource(menuItem.iconResId)
            textViewItemName.text = menuItem.title
            textViewItemName.setTextColor(Color.parseColor("#FFFFFF"))
            itemMenu.setBackgroundColor(Color.parseColor("#FF5252"))

            itemMenu.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }

    fun submitList(MenuItems: List<MenuItem>) {
        differ.submitList(MenuItems)
    }

}




