package com.example.cowboysstore.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.ItemRecyclerCarouselBinding

class RecyclerCarouselAdapter : RecyclerView.Adapter<RecyclerCarouselAdapter.RecyclerCarouselViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerCarouselViewHolder =
        RecyclerCarouselViewHolder(
            ItemRecyclerCarouselBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerCarouselViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class RecyclerCarouselViewHolder(private val itemBinding: ItemRecyclerCarouselBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(image: String) {
            itemBinding.imageViewProductPreview.load(image) {
                crossfade(true)
                transformations(RoundedCornersTransformation(16f))
                error(R.drawable.no_data)
                placeholder(R.drawable.no_data)
            }
        }
    }

    fun submitList(descriptions : List<String>) {
        differ.submitList(descriptions)
    }

}