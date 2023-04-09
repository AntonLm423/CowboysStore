package com.example.cowboysstore.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cowboysstore.databinding.ItemProductStructureBinding


class ProductStructureAdapter : RecyclerView.Adapter<ProductStructureAdapter.ProductStructureViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductStructureViewHolder =
         ProductStructureViewHolder(
            ItemProductStructureBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ProductStructureViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ProductStructureViewHolder(private val itemBinding: ItemProductStructureBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(structure: String) {
            itemBinding.textViewStructure.text = "● $structure"
        }
    }

    fun submitList(descriptions : List<String>) {
        differ.submitList(descriptions)
    }

}