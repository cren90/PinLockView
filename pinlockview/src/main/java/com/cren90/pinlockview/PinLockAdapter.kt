package com.cren90.pinlockview

import android.content.Context
import android.graphics.PorterDuff
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * Created by aritraroy on 31/05/16.
 */
class PinLockAdapter(private val mContext: Context) : RecyclerView.Adapter<ViewHolder>() {
    var customizationOptions: CustomizationOptionsBundle? = null
    var onItemClickListener: OnNumberClickListener? = null
    var onDeleteClickListener: OnDeleteClickListener? = null
    var onSubmitClickListener: OnSubmitClickListener? = null
    private var mKeyValues: IntArray
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val viewHolder: ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        viewHolder = when (viewType) {
            VIEW_TYPE_NUMBER -> {
                val view = inflater.inflate(R.layout.layout_number_item,
                                            parent,
                                            false)
                NumberViewHolder(view)
            }
            VIEW_TYPE_DELETE -> {
                val view = inflater.inflate(R.layout.layout_delete_item,
                                            parent,
                                            false)
                DeleteViewHolder(view)
            }
            else             -> {
                val view = inflater.inflate(R.layout.layout_submit_item,
                                            parent,
                                            false)
                SubmitViewHolder(view)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_NUMBER -> {
                val vh = holder as NumberViewHolder
                configureNumberButtonHolder(vh, position)
            }
            VIEW_TYPE_DELETE -> {
                val vh = holder as DeleteViewHolder
                configureDeleteButtonHolder(vh)
            }
            VIEW_TYPE_SUBMIT -> {
                val vh = holder as SubmitViewHolder
                configureSubmitButtonHolder(vh)
            }
        }
    }

    private fun configureNumberButtonHolder(viewHolder: NumberViewHolder?, position: Int) {
        viewHolder?.let { holder ->
            holder.numberButton.text = mKeyValues[position].toString()
            holder.numberButton.visibility = View.VISIBLE
            holder.numberButton.tag = mKeyValues[position]

            customizationOptions?.let { options ->
                holder.numberButton.setTextColor(options.textColor)
                options.buttonBackgroundDrawable?.let {
                    holder.numberButton.background = it
                }
                holder.numberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                options.textSize
                                                    .toFloat())
                val params = LinearLayout.LayoutParams(
                        options.buttonSize,
                        options.buttonSize)
                holder.numberButton.layoutParams = params
            }
        }
    }

    private fun configureDeleteButtonHolder(viewHolder: DeleteViewHolder?) {
        viewHolder?.let { holder ->
            customizationOptions?.let { options ->
                if (options.isShowDeleteButton) {
                    holder.buttonImage.visibility = View.VISIBLE
                    options.deleteButtonDrawable?.let {
                        holder.buttonImage.setImageDrawable(it)
                    }

                    holder.buttonImage.setColorFilter(options.deleteButtonTintColor,
                                                      PorterDuff.Mode.SRC_ATOP)

                    options.deleteButtonBackgroundDrawable?.let {
                        holder.buttonImage.background = it
                    } ?: options.buttonBackgroundDrawable?.let {
                        holder.buttonImage.background = it
                    }

                    val params = LinearLayout.LayoutParams(
                            options.buttonSize,
                            options.buttonSize)
                    holder.buttonImage.layoutParams = params
                    holder.buttonImage.contentDescription = options.keypadDeleteButtonContentDescription
                } else {
                    holder.buttonImage.visibility = View.GONE
                }
            }
        }
    }

    private fun configureSubmitButtonHolder(viewHolder: SubmitViewHolder?) {
        viewHolder?.let { holder ->
            customizationOptions?.let { options ->
                if (options.isShowSubmitButton) {
                    holder.buttonImage.visibility = View.VISIBLE
                    if (options.submitButtonDrawable != null) {
                        holder.buttonImage.setImageDrawable(options.submitButtonDrawable)
                    }
                    holder.buttonImage.setColorFilter(options.submitButtonTintColor,
                                                      PorterDuff.Mode.SRC_ATOP)

                    options.submitButtonBackgroundDrawable?.let {
                        holder.buttonImage.background = it
                    } ?: options.buttonBackgroundDrawable?.let {
                        holder.buttonImage.background = it
                    }
                    val params = LinearLayout.LayoutParams(
                            options.submitButtonSize,
                            options.submitButtonSize)
                    holder.buttonImage.layoutParams = params
                    holder.buttonImage.contentDescription = options.keypadSubmitButtonContentDescription
                } else {
                    holder.buttonImage.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 12
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 3 -> VIEW_TYPE_DELETE
            itemCount - 1 -> VIEW_TYPE_SUBMIT
            else          -> VIEW_TYPE_NUMBER
        }
    }

    var keyValues: IntArray?
        get() = mKeyValues
        set(keyValues) {
            mKeyValues = getAdjustKeyValues(keyValues)
            notifyDataSetChanged()
        }

    private fun getAdjustKeyValues(keyValues: IntArray?): IntArray {
        val adjustedKeyValues = IntArray(keyValues!!.size + 1)
        for (i in keyValues.indices) {
            if (i < 9) {
                adjustedKeyValues[i] = keyValues[i]
            } else {
                adjustedKeyValues[i] = -1
                adjustedKeyValues[i + 1] = keyValues[i]
            }
        }
        return adjustedKeyValues
    }

    inner class NumberViewHolder(itemView: View) : ViewHolder(itemView) {
        var numberButton: Button = itemView.findViewById<View>(R.id.button) as Button

        init {
            numberButton.setOnClickListener { v ->
                onItemClickListener?.onNumberClicked(v.tag as Int)
            }
        }
    }

    inner class DeleteViewHolder(itemView: View) : ViewHolder(itemView) {
        var buttonImage = itemView.findViewById<ImageView>(R.id.buttonImage)

        init {
            if (customizationOptions?.isShowDeleteButton == true) {
                buttonImage.setOnClickListener {
                    onDeleteClickListener?.onDeleteClicked()
                }
                buttonImage.setOnLongClickListener {
                    onDeleteClickListener?.onDeleteLongClicked()
                    true
                }
            }
        }
    }

    inner class SubmitViewHolder(itemView: View) : ViewHolder(itemView) {
        var buttonImage = itemView.findViewById<ImageView>(R.id.buttonImage)

        init {
            if (customizationOptions?.isShowDeleteButton == true) {
                buttonImage.setOnClickListener {
                    onSubmitClickListener?.onSubmitClicked()
                }
            }
        }
    }

    interface OnNumberClickListener {
        fun onNumberClicked(keyValue: Int)
    }

    interface OnDeleteClickListener {
        fun onDeleteClicked()
        fun onDeleteLongClicked()
    }

    interface OnSubmitClickListener {
        fun onSubmitClicked()
    }

    companion object {
        private const val VIEW_TYPE_NUMBER = 0
        private const val VIEW_TYPE_DELETE = 1
        private const val VIEW_TYPE_SUBMIT = 2
    }

    init {
        mKeyValues = getAdjustKeyValues(intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0))
    }
}