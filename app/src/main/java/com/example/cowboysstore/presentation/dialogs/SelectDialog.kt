package com.example.cowboysstore.presentation.dialogs

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cowboysstore.databinding.DialogSelectBinding
import com.example.cowboysstore.presentation.adapters.SelectingItemAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class SelectDialog(
    context: Context,
    private val itemsForSelection: List<String>,
) : BottomSheetDialog(context) {

    private lateinit var binding: DialogSelectBinding
    val selectingItemAdapter by lazy { SelectingItemAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewAvailableSizes.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = selectingItemAdapter
        }

        selectingItemAdapter.submitList(itemsForSelection)
    }
}