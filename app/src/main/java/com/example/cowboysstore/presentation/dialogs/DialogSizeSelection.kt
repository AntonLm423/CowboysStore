package com.example.cowboysstore.presentation.dialogs

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cowboysstore.databinding.DialogSizeSelectionBinding
import com.example.cowboysstore.presentation.adapters.SizeSelectionAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class DialogSizeSelection(
    context : Context,
    private val availableSizes : List<String>
) : BottomSheetDialog(context) {

    private var onSizeItemClickListener: OnSizeItemClickListener? = null

    private lateinit var binding : DialogSizeSelectionBinding
    private val sizeSelectionAdapter by lazy { SizeSelectionAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSizeSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewAvailableSizes.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = sizeSelectionAdapter
        }

        sizeSelectionAdapter.submitList(availableSizes)
    }

    fun setOnItemClickListener(listener: OnSizeItemClickListener) {
        onSizeItemClickListener = listener
    }

    interface OnSizeItemClickListener {
        fun onItemClick(item: String)
    }
}