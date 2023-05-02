package com.example.cowboysstore.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.ItemPagerCarouselBinding

class ProductPreviewCarouselAdapter : RecyclerView.Adapter<ProductPreviewCarouselAdapter.ViewPagerViewHolder>() {

    private var images : List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductPreviewCarouselAdapter.ViewPagerViewHolder =
        ViewPagerViewHolder(
            ItemPagerCarouselBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bind(images[position])
    }

    inner class ViewPagerViewHolder(private val itemBinding: ItemPagerCarouselBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(image : String) = with(itemBinding) {

            imageViewPreview.load(image) {
                crossfade(true)
                transformations(RoundedCornersTransformation(8f))
                error(R.drawable.no_data)
                placeholder(R.drawable.no_data)
            }
        }
    }

    fun submitList(images : List<String>) {
        this.images = images
    }

}