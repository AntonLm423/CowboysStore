package com.example.cowboysstore.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.ItemProductPreviewBinding

class ProductPreviewAdapter : RecyclerView.Adapter<ProductPreviewAdapter.ProductPreviewViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }

    var itemClickListener : (Int) -> Unit = {}

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductPreviewViewHolder =
        ProductPreviewViewHolder(
            ItemProductPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ProductPreviewViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ProductPreviewViewHolder(private val itemBinding: ItemProductPreviewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(image: String) {
            itemBinding.imageViewProductPreview.load(image) {
                crossfade(true)
                error(R.drawable.no_data)
                placeholder(R.drawable.no_data)
            }

            itemBinding.root.setOnClickListener {
                itemClickListener.invoke(bindingAdapterPosition)
            }
        }
    }

    fun submitList(descriptions : List<String>) {
        differ.submitList(descriptions)
    }

}