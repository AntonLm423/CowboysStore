package com.example.cowboysstore.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.cowboysstore.R
import com.example.cowboysstore.domain.entities.Order
import com.example.cowboysstore.databinding.ItemOrderBinding
import com.example.cowboysstore.utils.Constants
import java.text.SimpleDateFormat

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    companion object {
        const val STATUS_IN_WORK = "in_work"
        const val STATUS_CANCELED = "cancelled"
        const val STATUS_LOADING = "loading"
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    var popupMenuCancelClickListener: (Int) -> Unit = { }

    inner class OrderViewHolder(private val itemBinding: ItemOrderBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(order: Order) {
            itemBinding.apply {
                textViewOrderDate.text = itemView.context.getString(
                    R.string.orders_order_from,
                    order.number,
                    localeDateFormatter(itemView.context, order.createdAt, R.string.orders_order_from_format)
                )

                textViewDeliveryDate.text = itemView.context.getString(
                    R.string.orders_delivery_date,
                    localeDateFormatter(itemView.context, order.etd, R.string.orders_delivery_date_format)
                )

                textViewOrderTitle.text = order.productId

                when (order.status) {
                    STATUS_IN_WORK -> {
                        textViewOrderStatus.text = itemView.context.getString(R.string.orders_order_status_in_work)
                        textViewOrderStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.green_success))
                        progressPopupButton.setOnClickListener { showPopupMenu(progressPopupButton) }
                        textViewDeliveryAddress.text =
                            itemView.context.getString(R.string.orders_text_delivery_address, order.deliveryAddress)
                        progressPopupButton.tag = bindingAdapterPosition
                    }
                    STATUS_CANCELED -> {
                        textViewOrderStatus.text = itemView.context.getString(R.string.orders_order_status_canceled)
                        textViewOrderStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.red_failure))
                        progressPopupButton.visibility = View.GONE
                        imageViewOrderPreview.alpha = 0.2f
                        textViewDeliveryDate.text = itemView.context.getString(
                            R.string.orders_cancel,
                            localeDateFormatter(itemView.context, order.etd, R.string.orders_delivery_date_format)
                        )
                        textViewDeliveryAddress.visibility = View.INVISIBLE
                    }
                    STATUS_LOADING -> {
                        textViewOrderStatus.text = itemView.context.getString(R.string.orders_order_status_in_work)
                        textViewOrderStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.green_success))
                        textViewDeliveryAddress.text =
                            itemView.context.getString(R.string.orders_text_delivery_address, order.deliveryAddress)
                        progressPopupButton.isLoading = false
                        progressPopupButton.setOnClickListener(null)
                        progressPopupButton.tag = null
                    }
                }

                imageViewOrderPreview.load(order.productPreview) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation(16f))
                    error(R.drawable.no_data)
                    placeholder(R.drawable.no_data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder = OrderViewHolder(
        ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(order: List<Order>) {
        differ.submitList(order)
    }

    private fun localeDateFormatter(
        context: Context, // контекст для получения строк из ресурсов
        inputDate: String, // входная строка
        dateFormatResId: Int, // формат выходной строки
    ): String {
        val inputDateFormat = SimpleDateFormat(Constants.INPUT_DATE_FORMAT)
        val outputDateFormat = SimpleDateFormat(context.getString(dateFormatResId))
        val date = inputDateFormat.parse(inputDate)
        return (requireNotNull(outputDateFormat.format(date)))
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menu.add(R.string.orders_popup_cancel)
        popupMenu.setOnMenuItemClickListener {
            popupMenuCancelClickListener.invoke(view.tag as Int) // передаём позицию элемента, tag устанавливался в методе Bind
            true
        }
        popupMenu.show()
    }
}
