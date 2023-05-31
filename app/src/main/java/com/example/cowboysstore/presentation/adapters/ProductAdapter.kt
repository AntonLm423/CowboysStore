package com.example.cowboysstore.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.ItemProductBinding
import com.example.cowboysstore.domain.entities.Product

class ProductAdapter(
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    var onClickListener: (String) -> Unit = {}
    var onButtonBuyClickListener: (Product) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ProductViewHolder(private val itemBinding: ItemProductBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(product: Product) = with(itemBinding) {
            textViewTitle.text = product.title
            textViewCategory.text = product.department
            textViewPrice.text = itemView.context.getString(R.string.price_template, product.price)

            imageViewPreview.load(product.preview) {
                crossfade(true)
                transformations(RoundedCornersTransformation(16f))
                error(R.drawable.no_data)
                placeholder(R.drawable.no_data)
            }

            itemProduct.setOnClickListener {
                onClickListener(product.id)
            }

            buttonBuy.setOnClickListener {
                onButtonBuyClickListener(product)
            }
        }
    }

    fun submitList(product: List<Product>) {
        differ.submitList(product)
    }
}

