package com.cren90.pinlockview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cren90.pinlockview.PinLockAdapter.OnDeleteClickListener
import com.cren90.pinlockview.PinLockAdapter.OnNumberClickListener

/**
 * Represents a numeric lock view which can used to taken numbers as input.
 * The length of the input can be customized using [PinLockView.setPinLength], the default value being 4
 *
 *
 * It can also be used as dial pad for taking number inputs.
 * Optionally, [Indicator] can be attached to this view to indicate the length of the input taken
 * Created by aritraroy on 31/05/16.
 */
class PinLockView : RecyclerView {
    var pin = ""
        set(value) {
            field = value
            if (isIndicatorAttached) {
                indicator?.pin = value
            }
        }
    var pinLength = 0
        set(value) {
            field = value
            if (isIndicatorAttached) {
                indicator?.pinLength = value
            }
        }
    private var horizontalSpacing = 0
    private var verticalSpacing = 0

    var textColor = 0
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    textColor = value)
            customizationOptionsBundle = options
        }
    var textSize = 0
        set(value) {
            field = value
            val options = (customizationOptionsBundle
                           ?: CustomizationOptionsBundle()).copy(textSize = value)
            customizationOptionsBundle = options
        }

    var buttonSize = 0
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    buttonSize = value)
            customizationOptionsBundle = options
        }
    var buttonBackgroundDrawable: Drawable? = null
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    buttonBackgroundDrawable = value)
            customizationOptionsBundle = options
        }
    var buttonElevation = 0
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    buttonElevation = value)
            customizationOptionsBundle = options
        }

    var deleteButtonDrawable: Drawable? = null
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    deleteButtonDrawable = value)
            customizationOptionsBundle = options
        }
    var deleteButtonBackgroundDrawable: Drawable? = null
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    deleteButtonBackgroundDrawable = value)
            customizationOptionsBundle = options
        }
    var deleteButtonSize = 0
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    deleteButtonSize = value)
            customizationOptionsBundle = options
        }
    var showDeleteButton = false
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    isShowDeleteButton = value)
            customizationOptionsBundle = options
        }
    var deleteButtonTintColor = 0
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    deleteButtonTintColor = value)
            customizationOptionsBundle = options
        }
    var deleteButtonElevation = 0
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    deleteButtonElevation = value)
            customizationOptionsBundle = options
        }

    var keypadDeleteButtonContentDescription: String? = null
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    keypadDeleteButtonContentDescription = value)
            customizationOptionsBundle = options
        }

    var submitButtonDrawable: Drawable? = null
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    submitButtonDrawable = value)
            customizationOptionsBundle = options
        }
    var submitButtonBackgroundDrawable: Drawable? = null
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    submitButtonBackgroundDrawable = value)
            customizationOptionsBundle = options
        }
    var submitButtonSize = 0
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    submitButtonSize = value)
            customizationOptionsBundle = options
        }
    var showSubmitButton = false
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    isShowSubmitButton = value)
            customizationOptionsBundle = options
        }
    var submitButtonTintColor = 0
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    submitButtonTintColor = value)
            customizationOptionsBundle = options
        }
    var submitButtonElevation = 0
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    submitButtonElevation = value)
            customizationOptionsBundle = options
        }

    var keypadSubmitButtonContentDescription: String? = null
        set(value) {
            field = value
            val options = (customizationOptionsBundle ?: CustomizationOptionsBundle()).copy(
                    keypadSubmitButtonContentDescription = value)
            customizationOptionsBundle = options
        }

    private var indicator: Indicator? = null

    private var adapter: PinLockAdapter? = null
    var pinLockListener: PinLockListener? = null
    private var customizationOptionsBundle: CustomizationOptionsBundle? = null
        set(value) {
            field = value
            adapter?.customizationOptions = value
            adapter?.notifyDataSetChanged()
        }
    var customKeySet: IntArray? = null
        set(value) {
            field = value
            adapter?.keyValues = value
        }

    private val onNumberClickListener = object : OnNumberClickListener {
        override fun onNumberClicked(keyValue: Int) {

            if (pin.length < pinLength) {
                pin += keyValue.toString()

                if (pin.length == pinLength) {
                    pinLockListener?.onComplete(pin)
                } else {
                    pinLockListener?.onPinChange(pin.length, pin)
                }

            } else {
                if (!showDeleteButton) {
                    resetPinLockView()
                    pin += keyValue.toString()
                    pinLockListener?.onPinChange(pin.length, pin)

                } else {
                    pinLockListener?.onComplete(pin)
                }
            }
        }
    }
    private val deleteClickListener: OnDeleteClickListener = object : OnDeleteClickListener {
        override fun onDeleteClicked() {
            if (pin.isNotEmpty()) {
                pin = pin.substring(0, pin.length - 1)
                if (pin.isEmpty()) {
                    pinLockListener?.onEmpty()
                    clearInternalPin()
                } else {
                    pinLockListener?.onPinChange(pin.length, pin)
                }
            } else {
                pinLockListener?.onEmpty()
            }
        }

        override fun onDeleteLongClicked() {
            resetPinLockView()
            pinLockListener?.onEmpty()
        }
    }

    private val onSubmitClickListener = object : PinLockAdapter.OnSubmitClickListener {
        override fun onSubmitClicked() {
            if (pin.isNotEmpty() && pin.length == pinLength) {
                pinLockListener?.onPinSubmit(pin)
            }
        }
    }

    constructor(context: Context?) : super(context!!) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
            context!!,
            attrs) {
        init(attrs, 0)
    }

    constructor(context: Context?,
                attrs: AttributeSet?,
                defStyle: Int) : super(context!!, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attributeSet: AttributeSet?, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.PinLockView)
        try {
            pinLength = typedArray.getInt(R.styleable.PinLockView_pinLength,
                                          DEFAULT_PIN_LENGTH)
            horizontalSpacing = typedArray.getDimension(R.styleable.PinLockView_keypadHorizontalSpacing,
                                                        ResourceUtils.getDimensionInPx(
                                                                context,
                                                                R.dimen.default_horizontal_spacing))
                .toInt()
            verticalSpacing = typedArray.getDimension(R.styleable.PinLockView_keypadVerticalSpacing,
                                                      ResourceUtils.getDimensionInPx(
                                                              context,
                                                              R.dimen.default_vertical_spacing))
                .toInt()


            textColor = typedArray.getColor(R.styleable.PinLockView_keypadTextColor,
                                            ResourceUtils.getColor(context,
                                                                   R.color.white))
            textSize = typedArray.getDimension(R.styleable.PinLockView_keypadTextSize,
                                               ResourceUtils.getDimensionInPx(context,
                                                                              R.dimen.default_text_size))
                .toInt()


            buttonSize = typedArray.getDimension(R.styleable.PinLockView_keypadButtonSize,
                                                 ResourceUtils.getDimensionInPx(
                                                         context,
                                                         R.dimen.default_button_size)).toInt()
            buttonBackgroundDrawable = typedArray.getDrawable(R.styleable.PinLockView_keypadButtonBackgroundDrawable)
            buttonElevation = typedArray.getDimension(R.styleable.PinLockView_keypadButtonElevation,
                                                      ResourceUtils.getDimensionInPx(
                                                              context,
                                                              R.dimen.default_elevation)).toInt()


            deleteButtonDrawable = typedArray.getDrawable(R.styleable.PinLockView_keypadDeleteButtonDrawable)
            deleteButtonBackgroundDrawable = typedArray.getDrawable(R.styleable.PinLockView_keypadDeleteButtonBackgroundDrawable)
            deleteButtonSize = typedArray.getDimension(R.styleable.PinLockView_keypadDeleteButtonSize,
                                                       buttonSize.toFloat())
                .toInt()
            showDeleteButton = typedArray.getBoolean(R.styleable.PinLockView_keypadShowDeleteButton,
                                                     true)
            deleteButtonTintColor = typedArray.getColor(R.styleable.PinLockView_keypadDeleteButtonTintColor,
                                                        ResourceUtils.getColor(
                                                                context,
                                                                R.color.greyish))
            deleteButtonElevation = typedArray.getDimension(R.styleable.PinLockView_keypadDeleteButtonElevation,
                                                            ResourceUtils.getDimensionInPx(
                                                                    context,
                                                                    R.dimen.default_elevation))
                .toInt()


            submitButtonDrawable = typedArray.getDrawable(R.styleable.PinLockView_keypadSubmitButtonDrawable)
            submitButtonBackgroundDrawable = typedArray.getDrawable(R.styleable.PinLockView_keypadSubmitButtonBackgroundDrawable)
            submitButtonSize = typedArray.getDimension(R.styleable.PinLockView_keypadSubmitButtonSize,
                                                       buttonSize.toFloat())
                .toInt()
            showSubmitButton = typedArray.getBoolean(R.styleable.PinLockView_keypadShowSubmitButton,
                                                     true)
            submitButtonTintColor = typedArray.getColor(R.styleable.PinLockView_keypadSubmitButtonTintColor,
                                                        ResourceUtils.getColor(
                                                                context,
                                                                R.color.greyish))
            submitButtonElevation = typedArray.getDimension(R.styleable.PinLockView_keypadSubmitButtonElevation,
                                                            ResourceUtils.getDimensionInPx(
                                                                    context,
                                                                    R.dimen.default_elevation))
                .toInt()
        } finally {
            typedArray.recycle()
        }

        val optionsBundle = customizationOptionsBundle ?: CustomizationOptionsBundle().copy(
                textColor = textColor,
                textSize = textSize,
                buttonSize = buttonSize,
                buttonBackgroundDrawable = buttonBackgroundDrawable,
                buttonElevation = buttonElevation,
                deleteButtonDrawable = deleteButtonDrawable,
                deleteButtonBackgroundDrawable = deleteButtonBackgroundDrawable,
                deleteButtonSize = deleteButtonSize,
                isShowDeleteButton = showDeleteButton,
                deleteButtonTintColor = deleteButtonTintColor,
                deleteButtonElevation = deleteButtonElevation,
                submitButtonDrawable = submitButtonDrawable,
                submitButtonBackgroundDrawable = submitButtonBackgroundDrawable,
                submitButtonSize = submitButtonSize,
                isShowSubmitButton = showSubmitButton,
                submitButtonTintColor = submitButtonTintColor,
                submitButtonElevation = submitButtonElevation)

        customizationOptionsBundle = optionsBundle

        initView()
    }

    private fun initView() {
        layoutManager = LTRGridLayoutManager(context, 3)
        adapter = PinLockAdapter(context)
        adapter?.onItemClickListener = onNumberClickListener
        adapter?.onDeleteClickListener = deleteClickListener
        adapter?.onSubmitClickListener = onSubmitClickListener
        adapter?.customizationOptions = customizationOptionsBundle
        addItemDecoration(ItemSpaceDecoration(horizontalSpacing, verticalSpacing, 3, false))
        setAdapter(adapter)
        overScrollMode = View.OVER_SCROLL_NEVER
    }


    fun enableLayoutShuffling() {
        customKeySet = ShuffleArrayUtils.shuffle(DEFAULT_KEY_SET)
        adapter?.keyValues = customKeySet
    }

    private fun clearInternalPin() {
        pin = ""
    }

    /**
     * Resets the [PinLockView], clearing the entered pin
     * and resetting the [Indicator] if attached
     */
    fun resetPinLockView() {
        clearInternalPin()
    }

    /**
     * Returns true if [Indicator] are attached to [PinLockView]
     *
     * @return true if attached, false otherwise
     */
    val isIndicatorAttached: Boolean
        get() = indicator != null

    /**
     * Attaches [Indicator] to [PinLockView]
     *
     * @param indicator the view to attach
     */
    fun attachIndicator(indicator: Indicator?) {
        this.indicator = indicator
    }

    companion object {
        private const val DEFAULT_PIN_LENGTH = 4
        private val DEFAULT_KEY_SET = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
    }
}