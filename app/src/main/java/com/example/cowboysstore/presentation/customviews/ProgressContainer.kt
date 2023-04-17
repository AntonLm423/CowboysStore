package com.example.cowboysstore.presentation.customviews

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import com.example.cowboysstore.R
import kotlin.properties.Delegates

private const val MAX_CHILD_COUNT = 3

class ProgressContainer @JvmOverloads constructor(
    context : Context,
    attrs: AttributeSet? = null,
    defStyleAttr : Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val layoutLoading : ViewGroup
    private val layoutNotice : ViewGroup

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.view_progress_container, this, true)
        layoutLoading = root.findViewById(R.id.layoutLoading)
        layoutNotice = root.findViewById(R.id.layoutNotice)
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        if(childCount > MAX_CHILD_COUNT) {
            throw IllegalStateException("Progress container can host only one direct child")
        }
    }

    private fun findContextView() : View? = children.firstOrNull {
        it.id != R.id.layoutLoading && it.id != R.id.layoutNotice
    }

    var state : State by Delegates.observable(State.Loading) { _, _, state ->
        when(state) {
            is State.Loading -> {
                layoutLoading.isVisible = true
                layoutNotice.isVisible = false
                findContextView()?.isVisible = false
                Log.d("TAG_TEST", "State: Loading")
            }
            is State.Notice -> {
                layoutLoading.isVisible = false
                layoutNotice.isVisible = true
                findContextView()?.isVisible = false

                /* Устанавливаем тексты ошибок*/
                val textViewError = layoutNotice.findViewById<TextView>(R.id.textViewError)
                textViewError.text = context.getString(state.errorResId)
                val textViewMessage = layoutNotice.findViewById<TextView>(R.id.textViewMessage)
                textViewMessage.text = context.getString(state.messageResId)

                /* Если на кнопке не установлен обработчик, то она не видна */
                val button = layoutNotice.findViewById<Button>(R.id.buttonRefresh)
                if (state.buttonHandler != null) {
                    button.isVisible = true
                    button.setOnClickListener(state.buttonHandler)
                } else {
                    button.isVisible = false
                }
                Log.d("TAG_TEST", "State: Notice")
            }
            is State.Success -> {
                layoutLoading.isVisible = false
                layoutNotice.isVisible = false
                findContextView()?.isVisible = true
                Log.d("TAG_TEST", "State: Success")
            }
        }

    }
    sealed class State {
        object Loading : State()

        data class Notice(
            val errorResId: Int,
            val messageResId : Int,
            val buttonHandler: OnClickListener? = null
        ) : State()

        object Success : State()
    }
}