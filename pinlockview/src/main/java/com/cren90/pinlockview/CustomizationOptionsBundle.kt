package com.cren90.pinlockview

import android.graphics.drawable.Drawable

/**
 * The customization options for the buttons in [PinLockView]
 * passed to the [PinLockAdapter] to decorate the individual views
 *
 * Created by cren90 on 3/26/2020.
 * inspired by aritaroy
 */
data class CustomizationOptionsBundle(
        var textColor: Int = 0,
        var textSize: Int = 0,
        var buttonSize: Int = 0,
        var buttonBackgroundDrawable: Drawable? = null,
        var buttonElevation: Int = 0,
        var deleteButtonDrawable: Drawable? = null,
        var deleteButtonBackgroundDrawable: Drawable? = null,
        var deleteButtonSize: Int = 0,
        var isShowDeleteButton: Boolean = false,
        var deleteButtonTintColor: Int = 0,
        var deleteButtonElevation: Int = 0,
        var keypadDeleteButtonContentDescription: String? = null,
        var submitButtonDrawable: Drawable? = null,
        var submitButtonBackgroundDrawable: Drawable? = null,
        var submitButtonSize: Int = 0,
        var isShowSubmitButton: Boolean = false,
        var submitButtonTintColor: Int = 0,
        var submitButtonElevation: Int = 0,
        var keypadSubmitButtonContentDescription: String? = null
)