package com.example.cowboysstore.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.cowboysstore.R
import com.example.cowboysstore.data.model.Order
import com.example.cowboysstore.databinding.ItemOrderBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(private val itemBinding: ItemOrderBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(order: Order) = with(itemBinding) {

            textViewOrderDate.text = "${itemView.context.getString(R.string.orders_text_order)} № ${order.number} " +
                    "${itemView.context.getString(R.string.orders_text_from)} ${formatDate(order.createdAt)}"
            textViewOrderTitle.text = order.productId
            textViewDeliveryDate.text = "${itemView.context.getString(R.string.orders_text_delivery_date)}: ${formatDate(order.etd)}"
            textViewDeliveryAddress.text = "${itemView.context.getString(R.string.orders_text_delivery_address)}: ${order.deliveryAddress} "


            if(order.status == "in_work") {
                textViewOrderStatus.text = itemView.context.getString(R.string.orders_order_status_in_work)
                textViewOrderStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.green_success))
            }
            else {
                textViewOrderStatus.text = itemView.context.getString(R.string.orders_order_status_canceled)
                textViewOrderStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.red_failure))
            }

            imageViewOrderPreview.load(order.productPreview) {
                crossfade(true)
                transformations(RoundedCornersTransformation(16f))
                error(R.drawable.no_data)
                placeholder(R.drawable.no_data)
            }
        }
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder =
        OrderViewHolder(
           ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size



    fun submitList(order: List<Order>) {
        differ.submitList(order)
    }

    private fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val date = inputFormat.parse(inputDate)

        val outputFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault())
        return outputFormat.format(date)
    }


}
