package com.example.cowboysstore.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cowboysstore.databinding.ItemSizeSelectionBinding

class SizeSelectionAdapter(): RecyclerView.Adapter<SizeSelectionAdapter.SizeSelectionViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }

    var itemClickListener : (Int) -> Unit = { }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeSelectionViewHolder =
        SizeSelectionViewHolder(
            ItemSizeSelectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SizeSelectionViewHolder, position: Int) {
        holder.bind(differ.currentList[position])

    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class SizeSelectionViewHolder(private val itemBinding: ItemSizeSelectionBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(size : String) = with(itemBinding) {
            textViewSize.text = size

            itemBinding.root.setOnClickListener {
                itemClickListener.invoke(bindingAdapterPosition)
            }
        }
    }

    fun submitList(availableSizes: List<String>) {
        differ.submitList(availableSizes)
    }


}