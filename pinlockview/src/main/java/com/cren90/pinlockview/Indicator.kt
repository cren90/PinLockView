package com.cren90.pinlockview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IntDef

/**
 * It represents a set of indicators which when attached with [PinLockView]
 * can be used to indicate the current length of the input
 *
 *
 * Created by cren90 on 3/26/2020.
 * inspired by aritaroy
 */
class Indicator @JvmOverloads constructor(context: Context,
                                          attrs: AttributeSet? = null,
                                          defStyleAttr: Int = 0) : LinearLayout(
        context,
        attrs,
        defStyleAttr) {
    @IntDef(IndicatorType.FIXED,
            IndicatorType.FILL,
            IndicatorType.FILL_WITH_VALUE,
            IndicatorType.FIXED_WITH_VALUE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class IndicatorType {
        companion object {
            const val FIXED = 0
            const val FILL = 1
            const val FIXED_WITH_VALUE = 2
            const val FILL_WITH_VALUE = 3
        }
    }

    var indicatorHeight = 0
        set(value) {
            field = value
            drawViews()
        }

    var indicatorWidth = 0
        set(value) {
            field = value
            drawViews()
        }

    var indicatorSpacing = 0
        set(value) {
            field = value
            drawViews()
        }
    var fillDrawable = 0
        set(value) {
            field = value
            drawViews()
        }
    var emptyDrawable = 0
        set(value) {
            field = value
            drawViews()
        }
    var pinLength = 0
        set(value) {
            field = value
            drawViews()
        }

    var pin = ""
        set(value) {
            field = value
            drawViews()
        }

    var pinTextSize: Int = ResourceUtils.getDimensionInPx(context,
                                                          R.dimen.default_indicator_text_size)
        .toInt()
        set(value) {
            field = value
            drawViews()
        }

    @IndicatorType
    var indicatorType = IndicatorType.FIXED
        set(value) {
            field = value
            drawViews()
        }

    private var indicatorPadding = 0

    private var textViewWidth = 0

    private var previousLength = 0

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // If the indicator type is not fixed

        val textView = TextView(context)
        textView.textSize = pinTextSize.toFloat()
        textView.setTextColor(Color.WHITE)
        textView.text = "8"
        textView.includeFontPadding = false
        textView.setPadding(0, 0, 0, 0)
        val tvParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT)
        tvParams.setMargins(indicatorSpacing, 0, indicatorSpacing, 0)
        tvParams.gravity = Gravity.CENTER
        textView.layoutParams = tvParams
        textView.includeFontPadding = false
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                                                           MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                                                            MeasureSpec.UNSPECIFIED)
        textView.measure(widthMeasureSpec, heightMeasureSpec)

        val params = this.layoutParams
        if (indicatorType == IndicatorType.FIXED_WITH_VALUE || indicatorType == IndicatorType.FILL_WITH_VALUE) {
            params.height = maxOf(textView.measuredHeight, indicatorHeight)
            textViewWidth = textView.measuredWidth
            params.width = 2 * pinLength * indicatorSpacing + pinLength * textViewWidth
            indicatorPadding = ((params.width / 4) - (pinLength * indicatorWidth)) / 2
        } else {
            params.height = indicatorHeight
        }


        requestLayout()
        drawViews()
    }

    fun drawViews() {

        removeAllViews()
        for (i in 0 until pinLength) {
            when (indicatorType) {
                IndicatorType.FIXED            -> {
                    val indicator = View(context)
                    if (pin.length > i) {
                        fillIndicator(indicator)
                    } else {
                        emptyIndicator(indicator)
                    }
                    val params = LayoutParams(
                            indicatorWidth,
                            indicatorHeight)
                    params.setMargins(indicatorSpacing, 0, indicatorSpacing, 0)
                    indicator.layoutParams = params
                    addView(indicator, i)
                }
                IndicatorType.FILL             -> {
                    if (pin.length > i) {
                        val indicator = View(context)
                        fillIndicator(indicator)
                        val params = LayoutParams(
                                indicatorWidth,
                                indicatorHeight)
                        params.setMargins(indicatorSpacing, 0, indicatorSpacing, 0)
                        indicator.layoutParams = params
                        addView(indicator, i)
                    }
                }
                IndicatorType.FIXED_WITH_VALUE -> {
                    if (pin.length > i) {
                        val textView = TextView(context)
                        textView.textSize = pinTextSize.toFloat()
                        textView.setTextColor(Color.WHITE)
                        textView.text = pin[i].toString()
                        textView.includeFontPadding = false
                        textView.setPadding(0, 0, 0, 0)
                        val params = LayoutParams(
                                textViewWidth,
                                LayoutParams.WRAP_CONTENT)
                        params.setMargins(indicatorSpacing, 0, indicatorSpacing, 0)
                        params.gravity = Gravity.CENTER
                        textView.layoutParams = params
                        addView(textView, i)
                    } else {
                        val indicator = View(context)
                        emptyIndicator(indicator)
                        val params = LayoutParams(
                                indicatorWidth,
                                indicatorHeight)
                        params.setMargins(indicatorSpacing + indicatorPadding,
                                          0,
                                          indicatorSpacing + indicatorPadding,
                                          0)
                        params.gravity = Gravity.CENTER
                        indicator.layoutParams = params
                        addView(indicator, i)
                    }
                }
                IndicatorType.FILL_WITH_VALUE  -> {
                    if (pin.length > i) {
                        val textView = TextView(context)
                        textView.textSize = pinTextSize.toFloat()
                        textView.setTextColor(Color.WHITE)
                        textView.text = pin[i].toString()
                        textView.includeFontPadding = false
                        textView.setPadding(0, 0, 0, 0)
                        val params = LayoutParams(
                                textViewWidth,
                                LayoutParams.WRAP_CONTENT)
                        params.setMargins(indicatorSpacing, 0, indicatorSpacing, 0)
                        params.gravity = Gravity.CENTER
                        textView.layoutParams = params
                        addView(textView, i)
                    }
                }
            }
        }
        requestLayout()
    }

    private fun emptyIndicator(view: View) {
        view.setBackgroundResource(emptyDrawable)
    }

    private fun fillIndicator(view: View) {
        view.setBackgroundResource(fillDrawable)
    }

    companion object {
        private const val DEFAULT_PIN_LENGTH = 4
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs,
                                                        R.styleable.Indicator)
        try {
            indicatorHeight = typedArray.getDimension(R.styleable.Indicator_indicatorHeight,
                                                      ResourceUtils.getDimensionInPx(
                                                              getContext(),
                                                              R.dimen.default_indicator_size))
                .toInt()
            indicatorWidth = typedArray.getDimension(R.styleable.Indicator_indicatorWidth,
                                                     ResourceUtils.getDimensionInPx(
                                                             getContext(),
                                                             R.dimen.default_indicator_size))
                .toInt()
            indicatorSpacing = typedArray.getDimension(R.styleable.Indicator_indicatorSpacing,
                                                       ResourceUtils.getDimensionInPx(
                                                               getContext(),
                                                               R.dimen.default_indicator_spacing))
                .toInt()
            fillDrawable = typedArray.getResourceId(R.styleable.Indicator_indicatorFilledBackground,
                                                    R.drawable.dot_filled)
            emptyDrawable = typedArray.getResourceId(R.styleable.Indicator_indicatorEmptyBackground,
                                                     R.drawable.dot_empty)
            pinLength = typedArray.getInt(R.styleable.PinLockView_pinLength,
                                          DEFAULT_PIN_LENGTH)
            indicatorType = typedArray.getInt(R.styleable.Indicator_indicatorType,
                                              IndicatorType.FIXED)

            pinTextSize = typedArray.getDimension(R.styleable.Indicator_indicatorTextSize,
                                                  ResourceUtils.getDimensionInPx(
                                                          context,
                                                          R.dimen.default_indicator_text_size))
                .toInt()
        } finally {
            typedArray.recycle()
        }

        drawViews()
    }
}