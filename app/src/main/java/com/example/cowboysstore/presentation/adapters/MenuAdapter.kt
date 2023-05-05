package com.example.cowboysstore.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.ItemMenuBinding

class MenuAdapter : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    var onItemClickListener: ((position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder =
        MenuViewHolder(
            ItemMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class MenuViewHolder(private val itemBinding: ItemMenuBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(menuItem : MenuItem) {
            when(menuItem) {
                is MenuItem.Regular -> bindRegularItem(menuItem)
                is MenuItem.Red -> bindRedItem(menuItem)
            }
        }

        private fun bindRegularItem(menuItem: MenuItem.Regular) = with(itemBinding) {
            imageViewPicture.setImageResource(menuItem.iconResId)
            textViewItemName.text = menuItem.title
            itemMenu.setBackgroundResource(R.color.item_menu_regular_selector)
            itemMenu.setOnClickListener {
               onItemClickListener?.invoke(adapterPosition)
           }
        }

        private fun bindRedItem(menuItem : MenuItem.Red) = with(itemBinding) {
            imageViewPicture.setImageResource(menuItem.iconResId)
            textViewItemName.text = menuItem.title
            textViewItemName.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            itemMenu.setBackgroundResource(R.color.item_menu_red_selector)
            itemMenu.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MenuItem>() {
        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(MenuItems: List<MenuItem>) {
        differ.submitList(MenuItems)
    }

    sealed class MenuItem {
        abstract val id: Int

        data class Regular(
            override val id: Int,
            val title: String,
            val iconResId: Int
        ) : MenuItem()

        data class Red(
            override val id: Int,
            val title: String,
            val iconResId: Int
        ) : MenuItem()
    }
}




